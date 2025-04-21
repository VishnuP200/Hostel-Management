package com.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.DTO.HostelDTO;
import com.Entity.Hostel;
import com.Entity.Status;
import com.PgConstants.Constants;
import com.exception.ResourceNotFoundException;
import com.exception.errordtls;
import com.feignClient.RoomClient;
import com.repo.HostelRepository;

import jakarta.transaction.Transactional;

@Service
public class HostelService {

	private static final Logger logger = LoggerFactory.getLogger(HostelService.class);

	@Autowired
	HostelRepository hostelRepository;

//	private final ModelMapper modelMapper;
//
//	@Autowired
//	public HostelService(ModelMapper modelMapper) {
//		this.modelMapper = modelMapper;
//	}

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RoomClient roomClient;

	public List<HostelDTO> getHostelsByLocation(String location, Double price) {
		logger.info("Entering getHostelsByLocation() with location={} and price={}", location, price);
		List<Hostel> hostels;

		if (location != null && price != null) {
			hostels = hostelRepository.findByLocationAndPriceLessThanEqualAndStatus(location, price, Status.AVAILABLE);
		} else if (location != null) {
			hostels = hostelRepository.findByLocationAndStatus(location, Status.AVAILABLE);
		} else if (price != null) {
			hostels = hostelRepository.findBypriceLessThanEqualAndStatus(price, Status.AVAILABLE);
		} else {
			hostels = hostelRepository.findAll();
		}
		if (hostels.isEmpty())
			throw new IllegalArgumentException(
					"No hostels are available in this location" + location + "for this price" + price);
		List<HostelDTO> hostelDTOs = hostels.stream().map(entity -> modelMapper.map(entity, HostelDTO.class))
				.collect(Collectors.toList());
		return hostelDTOs;
	}

	public List<HostelDTO> getHostelsByHostelNameAndHostelId(Long hostelId, String hostelName) {

		List<Hostel> hostels = new ArrayList<>();
		if (hostelId != null && hostelName != null) {
			hostels = hostelRepository.findByHostelIdOrHostelName(hostelId, hostelName);
		} else if (hostelId != null) {
			Optional<Hostel> optionalHostel = hostelRepository.findById(hostelId);
			hostels = optionalHostel.map(List::of).orElse(Collections.emptyList());
		} else if (hostelName != null) {
			hostels = hostelRepository.findByHostelIdOrHostelName(null, hostelName);
		} else {
			hostels = hostelRepository.findAll();
		}

		if (hostels.isEmpty())
			throw new IllegalArgumentException(
					"No hostels are available for this hostelId" + hostelId + "and hostelName" + hostelName);
		List<HostelDTO> hostelDTOs = hostels.stream().map(entity -> modelMapper.map(entity, HostelDTO.class))
				.collect(Collectors.toList());

		return hostelDTOs;
	}

	public String addHostels(List<HostelDTO> hostelDTOs) {

		if (hostelDTOs.isEmpty())
			throw new IllegalArgumentException("Data which you have sent is empty");
		List<Hostel> hostels = hostelDTOs.stream().map(dto -> {
			if (dto.getHostelId() != null && hostelRepository.existsByHostelId(dto.getHostelId())) {
				throw new IllegalStateException("Hostel Id already exisits" + dto.getHostelId());
			}
			Hostel hostel = modelMapper.map(dto, Hostel.class);
			return hostel;
		}).collect(Collectors.toList());
		hostelRepository.saveAll(hostels);
		return "Hostels are added successfully for given data";
	}

	public String updateHostel(List<HostelDTO> hostelDTOs) throws Exception {
		logger.info("Entering into updateHostel{}");
		List<errordtls> err = new ArrayList<>();
		try {
			List<Hostel> hostels = hostelDTOs.stream().map(dto -> {
				if (dto.getHostelId() == null) {
					err.add(new errordtls(Constants.ERROR_1, Constants.errMsg, String.valueOf(dto.getHostelId())));
					return null;
				}
				Optional<Hostel> optionalHostel = hostelRepository.findById(dto.getHostelId());
				if (optionalHostel.isEmpty()) {
					err.add(new errordtls(Constants.ERROR_1, Constants.errMsg, String.valueOf(dto.getHostelId())));
					return null;
				}
				Hostel exisitHostel = optionalHostel.get();
				exisitHostel.setHostelName(dto.getHostelName());
				exisitHostel.setLocation(dto.getLocation());
				exisitHostel.setStatus(dto.getStatus());
				exisitHostel.setPrice(dto.getPrice());
				exisitHostel.setHostelCapacity(dto.getHostelCapacity());
				return exisitHostel;
			}).filter(Objects::nonNull).collect(Collectors.toList());

			if (!err.isEmpty()) {
				logger.info("Entering into errors{}", err.toString());
				throw new ResourceNotFoundException(err);
			}
			hostelRepository.saveAll(hostels);
			return "Hostel data updated successfully";
		} catch (ResourceNotFoundException ex) {
			throw new ResourceNotFoundException(ex.getErrors());
		} catch (Exception exc) {
			throw new Exception("Hostels are not updated successfuully ");

		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@Transactional
	public String deleteHostel(Long hostelId, String hostelName) throws Exception {
		logger.info("Entering into deleteHostel{}", hostelName);
		try {
			if (hostelRepository.existsByHostelId(hostelId)) {
				ResponseEntity<String> roomstatus = roomClient.deleteRoom(null, hostelId);
				logger.info("deleteing rooms{}", roomstatus.getBody());
				if (hostelId != null && hostelName != null) {
					hostelRepository.deleteByHostelIdAndHostelName(hostelId, hostelName);
				} else if (hostelId != null) {
					hostelRepository.deleteById(hostelId);
				}
			} else {
				throw new Exception("hostelId is not found with" + hostelId);
			}

			return "Data deleted successfully for" + hostelId;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public boolean hostelExsists(Long hostelId) {
		return hostelRepository.existsByHostelId(hostelId);
	}

}
