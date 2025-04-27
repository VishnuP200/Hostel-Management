package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Dto.RoomDTO;
import com.entity.Room;
import com.entity.RoomId;
import com.entity.Status;
import com.exception.ResourceNotFoundException;
import com.exception.errordtls;
import com.feignClient.BedClient;
import com.feignClient.HostelClient;
import com.repository.RoomRepository;

import jakarta.transaction.Transactional;

@Service
public class RoomService {

	private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	HostelClient hostelClient;

	@Autowired
	BedClient bedClient;

	public List<RoomDTO> getAllAvailableRooms(Integer roomId, Long hostelId) throws Exception {
		logger.info("Entering itno getAvailableRooms{}", System.currentTimeMillis());
		List<Room> rooms = new ArrayList<>();
		List<RoomDTO> roomDtos = new ArrayList<>();
		try {

			if (hostelId != null && roomId != null) {
				logger.info("hostelId{} roomId{}", hostelId, roomId);
				Room r = roomRepository.findRoomByRoomIdAndHostelId(roomId, hostelId).orElse(null);
				rooms.add(r);
			} else if (hostelId != null) {
				logger.info("hostelId is {}", hostelId);
				rooms = roomRepository.findByRoomId_HostelIdAndStatus(hostelId, Status.AVAILABLE);
			} else {
				throw new IllegalArgumentException("Hostel Id cannot not be null.");
			}
			if (rooms.isEmpty()) {
				throw new Exception("rooms are empty for given hostelId" + hostelId);
			}
//			if (!rooms.isEmpty()) {
//				logger.info("entering rooms{}",rooms);
//				 roomDtos = rooms.stream().map(room -> {
//		                RoomDTO roomDTO = new RoomDTO();
//		                roomDTO.setRoomId(room.getRoomId().getRoomId());
//		                roomDTO.setHostelId(room.getRoomId().getHostelId());
//		                roomDTO.setPrice(room.getPrice());
//		                roomDTO.setRoomCapacity(room.getRoomCapacity());
//		                roomDTO.setStatus(room.getStatus().toString());
//		                return roomDTO;
//		            }).collect(Collectors.toList());

			if (!rooms.isEmpty()) {
				roomDtos = rooms.stream().map(room -> {
					RoomDTO roomDTO = new RoomDTO();
					roomDTO = modelMapper.map(room, RoomDTO.class);
					return roomDTO;
				}).collect(Collectors.toList());
			}
			return roomDtos;
		} catch (Exception ex) {
			logger.info("entering into error{}", ex.getMessage());
			throw new Exception("Rooms are not available for the particular Hostel" + hostelId);
		}

	}

	public String addRooms(List<RoomDTO> roomDTOs) {
		System.out.println("Entering into rooms");
		List<errordtls> err = new ArrayList<>();

		List<Room> rooms = roomDTOs.stream().map(dto -> {
			if (hostelClient.hostelExisits(dto.getHostelId())) {
				RoomId id = new RoomId(dto.getRoomId(), dto.getHostelId());
				Room room = modelMapper.map(dto, Room.class);
				room.setRoomId(id);
				return room;
			
			} else {
				err.add(new errordtls("err2", "hostelId is not exisits in hostelMS", dto.getHostelId().toString()));
				return null;
			}
			
		}).filter(Objects::nonNull).collect(Collectors.toList());

		if (!err.isEmpty()) {
//			throw new ResourceNotFoundException(err);
			throw new RuntimeException();
		}

		roomRepository.saveAll(rooms);

		return "Rooms added successfully";

	}

	public String updateRooms(List<RoomDTO> roomDTOs) {

		List<Room> rooms = roomDTOs.stream().map(dto -> {
			Room existingRoom = roomRepository.findRoomByRoomIdAndHostelId(dto.getRoomId(), dto.getHostelId())
					.orElseThrow(() -> new IllegalStateException("rooms are not available for" + dto.getRoomId()));
			RoomId roomId = new RoomId(dto.getRoomId(), dto.getHostelId());
			existingRoom.setRoomId(roomId);
			existingRoom.setPrice(dto.getPrice());
			existingRoom.setRoomCapacity(dto.getRoomCapacity());
			existingRoom.setStatus(dto.getStatus() != null ? Status.valueOf(dto.getStatus()) : Status.AVAILABLE);
			return existingRoom;

		}).collect(Collectors.toList());
		roomRepository.saveAll(rooms);

		return "Rooms updated Successfully";

	}

	public String deleteRoomByRoomId(Integer roomID, Long hostelId) throws Exception {
		List<Room> rooms = new ArrayList<>();
		try {
			Boolean hostelFlag = hostelClient.hostelExisits(hostelId);
			logger.info("hostelFlag{}", hostelFlag);
			System.out.println(hostelFlag);
			if (hostelFlag) {
				if (roomID != null && hostelId != null) {
					Room room = roomRepository.findRoomByRoomIdAndHostelId(roomID, hostelId)
							.orElseThrow(() -> new IllegalStateException("rooms are not available for" + roomID));
					rooms.add(room);
				} else if (hostelId != null) {
					rooms = roomRepository.findByRoomId_HostelId(hostelId);
				} else {
					throw new IllegalStateException("roomId and hostelName cannot be null");
				}
				if (!rooms.isEmpty()) {
					Set<ResponseEntity<String>> response = rooms.stream().map(room -> {
						try {
							return bedClient.deleteBeds(null, room.getRoomId().getRoomId(),
									room.getRoomId().getHostelId());
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}

					}).collect(Collectors.toSet());
				}

				roomRepository.deleteAll(rooms);
			} else {
				throw new Exception("HostelId is not exisits in hostel database" + hostelId);
			}
			return "Rooms are deleted successfully";
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}

	/***
	 * @Transactional public String updateHostels(Map<String,String> hstlMap) {
	 *                logger.info("Entering into updateHostels {}", hstlMap); //
	 *                List<Room> rooms = hstlMap.entrySet().stream().map(entry ->{
	 *                //
	 *                if(roomRepository.existsByRoomId_HostelName(entry.getKey())) {
	 *                // List<Room> exisistRooms =
	 *                roomRepository.findByRoomId_HostelName(entry.getKey()); //
	 *                logger.info("exisit rooms are{}",exisistRooms); // List<Room>
	 *                updateRoom = exisistRooms.stream().map(room -> { // RoomId
	 *                roomId = new
	 *                RoomId(room.getRoomId().getRoomId(),entry.getValue()); //
	 *                room.setRoomId(roomId); // return room; //
	 *                }).collect(Collectors.toList()); // return updateRoom; // } //
	 *                return new ArrayList<Room>(); //returing empty room if no
	 *                hostelName exisits //
	 *                }).flatMap(List::stream).collect(Collectors.toList()); //
	 *                logger.info("rooms are {} ", rooms.toString()); // //
	 *                List<RoomDTO> roomDTOs = rooms.stream().map(r ->
	 *                modelMapper.map(r,
	 *                RoomDTO.class)).collect(Collectors.toList());
	 * 
	 *                hstlMap.forEach((oldname, newname) ->{
	 *                if(roomRepository.existsByRoomId_HostelName(oldname)) {
	 *                roomRepository.updateHostelNameInRooms(oldname, newname); }
	 *                });
	 * 
	 *                return "rooms are updated with given hostelNames"; }
	 ***/

}
