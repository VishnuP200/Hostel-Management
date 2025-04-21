package com.openfeign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.Valid;

@FeignClient(name = "RoomServiceApp")
public interface RoomClient {

	@GetMapping("/room/available")
	public ResponseEntity<List<JsonNode>> getAvailableRooms(@Valid @RequestParam(required = false) Integer roomId,
			@Valid @RequestParam Long hostelId) throws Exception;

}
