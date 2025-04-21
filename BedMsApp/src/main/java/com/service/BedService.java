package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Bed.Entity.Bed;
import com.Bed.Entity.RoomBedHostelId;
import com.Bed.Entity.Status;
import com.DTO.BedDTO;
import com.DTO.RoomHostel;
import com.exception.ResourceNotFoundException;
import com.exception.errordtls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openfeign.RoomClient;
import com.repo.BedRepository;

@Service
public class BedService {

	private static final Logger logger = LoggerFactory.getLogger(BedService.class);

	@Autowired
	public BedRepository bedRepository;

	@Autowired
	RoomClient roomClient;

	@Autowired
	public ModelMapper modelMapper;

	@CachePut(value = "beds", key = "#bedDto.bedId + '_' + #bedDto.roomId + '_' + #bedDto.hostelId")
	public String addBedsInRoom(List<BedDTO> bedDtos) throws Exception {
		List<Bed> latestBeds = getLatestBeds(bedDtos);
		logger.info("latestbeds are{}", latestBeds);
		bedRepository.saveAll(latestBeds);
		return "beds added successfully";
	}

	public List<Bed> getLatestBeds(List<BedDTO> bedDTOs) throws Exception {
		Map<RoomHostel, Set<Integer>> exisitBedCount = new HashMap<>();
		List<errordtls> error = new ArrayList<>();

		List<Bed> beds = bedDTOs.stream().map(dto -> {
			Bed bed;
			Integer latestCount;
			RoomHostel id = new RoomHostel(dto.getRoomId(), dto.getHostelId());
			exisitBedCount.putIfAbsent(id, getUsedBedIds(dto.getRoomId(), dto.getHostelId()));
			Set<Integer> usedIds = exisitBedCount.get(id);
			if (dto.getBedId() != null) {
				latestCount = Integer.parseInt(dto.getBedId().replaceAll("[^0-9]", ""));
				usedIds.add(latestCount);
				bed = modelMapper.map(dto, Bed.class);
				bed.setId(new RoomBedHostelId(dto.getBedId(), dto.getRoomId(), dto.getHostelId()));

			} else {
				int next = usedIds.stream().max(Integer::compareTo).orElse(0);
				latestCount = next + 1;
				bed = modelMapper.map(dto, Bed.class);
				bed.setId(new RoomBedHostelId("B" + latestCount, dto.getRoomId(), dto.getHostelId()));
				usedIds.add(latestCount);
			}
			bed.setStatus(Status.fromString(dto.getStatus()));
			try {
				Integer roomCapacity = getRoomCapacity(dto.getRoomId(), dto.getHostelId());
				logger.info("latestCount is{}", latestCount);
				if (latestCount > roomCapacity) {
					error.add(new errordtls("ERROR5", "Bed Id cannot be greater than room capacity",
							String.valueOf(roomCapacity)));
					return null;
				}
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage());
			}
			return bed;
		}).filter(Objects::nonNull).collect(Collectors.toList());

		if (!error.isEmpty()) {
			throw new ResourceNotFoundException(error);
		}

		return beds;

	}

	public Set<Integer> getUsedBedIds(Integer roomId, Long hostelId) {
		Set<Integer> usedBedIds = bedRepository.findByRoomIdAndHostelId(roomId, hostelId).stream().map(bed -> {
			String bedId = bed.getId().getBedId();
			try {
				if (!bedId.matches("B\\d+")) {
					throw new IllegalArgumentException("Invalid bedId format: " + bedId);
				}
				return Integer.parseInt(bedId.replaceAll("[^0-9]", ""));
			} catch (NumberFormatException ex) {
				logger.warn("Failed to parse bedId: {}", bedId);
				return 0;
			}

		}).filter(num -> num > 0).collect(Collectors.toSet());
		logger.info("UsedBedIds are {}", usedBedIds);
		return usedBedIds;
	}

	public Integer getRoomCapacity(Integer roomId, Long hostelId) throws Exception {
		Integer roomCapacity = 0;
		ResponseEntity<List<JsonNode>> response = roomClient.getAvailableRooms(roomId, hostelId);
		if (response == null || response.getBody() == null || response.getBody().isEmpty()) {
			throw new Exception("No rooms found for roomId: " + roomId + " and hostelId: " + hostelId);
		}
		List<JsonNode> rooms = response.getBody();
		logger.info("rooms from roomMs{}", rooms.toString());
		JsonNode room = rooms.get(0);
		if (room.has("roomCapacity")) {
			roomCapacity = room.get("roomCapacity").asInt();
			logger.info("capacity is{}", roomCapacity);
		}
		return roomCapacity;

	}

	@Cacheable(value = "beds", key = "#bedId + '_'+ #roomId + '_' + #hostelId")
	public String getAllBeds(String bedId, Integer roomId, Long hostelId) throws JsonProcessingException {
		logger.info("Entering into getAllBeds{}", System.currentTimeMillis());
		List<Bed> beds = new ArrayList<>();
		ObjectMapper obj = new ObjectMapper();

		if (bedId != null && roomId != null && hostelId != null) {
			RoomBedHostelId id = new RoomBedHostelId(bedId, roomId, hostelId);
			Bed bed = bedRepository.findById(id).orElse(null);
			beds.add(bed);
		} else if (roomId != null && hostelId != null) {
			beds = bedRepository.findByRoomIdAndHostelIdAndStatus(roomId, hostelId, Status.AVAILABLE);
		} else {
			beds = bedRepository.findAllBedsByhostelId(hostelId, Status.AVAILABLE);
		}
		logger.info("beds are{}", beds);
		List<BedDTO> bedDTOs = beds.stream().map(bed -> {
			BedDTO dto = modelMapper.map(bed, BedDTO.class);
			dto.setRoomId(bed.getId().getRoomId());
			dto.setBedId(bed.getId().getBedId());
			dto.setHostelId(bed.getId().getHostelId());
			return dto;
		}).collect(Collectors.toList());

		String json = obj.writeValueAsString(bedDTOs);
		return json;

	}

	public String updateBed(List<BedDTO> bedDTOs) {
		List<errordtls> err = new ArrayList<>();
		List<Bed> beds = bedDTOs.stream().map(dto -> {

			if (dto.getBedId() == null) {
				err.add(new errordtls("Error2", "Bed Id shouldn't be null", dto.getBedId()));
				return null;
			}
			RoomBedHostelId id = new RoomBedHostelId(dto.getBedId(), dto.getRoomId(), dto.getHostelId());
			if (!bedRepository.existsById(id)) {
				err.add(new errordtls("ERROR3", "beds are not available for the give data as bedID:",
						dto.getBedId() + "HostelId : " + dto.getHostelId().toString()));
				return null;
			}
			Bed bed = modelMapper.map(dto, Bed.class);
			bed.setId(id);
			bed.setStatus(Status.fromString(dto.getStatus()));
			return bed;
		}).filter(Objects::nonNull).collect(Collectors.toList());

		if (!err.isEmpty())
			throw new ResourceNotFoundException(err);
		bedRepository.saveAll(beds);

		return "Beds are updated successfully";

	}

	@CacheEvict(value = "beds", allEntries = true)
	public String deleteBeds(String bedId, Integer roomId, Long hostelId) throws Exception {
		List<Bed> beds = new ArrayList<>();
		if (bedId != null && roomId != null && hostelId != null) {
			RoomBedHostelId id = new RoomBedHostelId(bedId, roomId, hostelId);
			Bed bed = bedRepository.findById(id).orElse(null);
			logger.info("exisiting bed is{}", bed);
			if (bed != null) {
				beds.add(bed);
			}
		} else if (roomId != null && hostelId != null) {
			beds = bedRepository.findByRoomIdAndHostelId(roomId, hostelId);
		}
		if (beds.isEmpty() || beds == null) {
			throw new Exception("beds are not available for the given room" + roomId + "and hsotelId" + hostelId);
		}
		bedRepository.deleteAll(beds);

		return "Bed deleted successfully In" + roomId;

	}

}
