package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.BedDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.BedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bed")
@Validated
public class BedController {

	@Autowired
	private BedService bedService;

	@PostMapping("/add")
	public ResponseEntity<String> addbeds(@Valid @RequestBody List<BedDTO> bedDTOs) throws Exception {
		return ResponseEntity.ok(bedService.addBedsInRoom(bedDTOs));
	}

	@GetMapping("/get/available")
	public ResponseEntity<String> getAvailableRooms(@RequestParam(required = false) String bedId,
			@RequestParam(required = false) Integer roomId, @RequestParam Long hostelId)
			throws JsonProcessingException {
		return ResponseEntity.ok(bedService.getAllBeds(bedId, roomId, hostelId));
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateBed(@Valid @RequestBody List<BedDTO> bedDTOs) {
		return ResponseEntity.ok(bedService.updateBed(bedDTOs));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBeds(@RequestParam(required = false) String bedId, @RequestParam Integer roomId,
			@RequestParam Long hostelId) throws Exception {
		return ResponseEntity.ok(bedService.deleteBeds(bedId, roomId, hostelId));
	}

}
