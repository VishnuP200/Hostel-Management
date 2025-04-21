package com.roomcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.Dto.RoomDTO;
import com.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/room")
@Validated
public class RoomController {

	@Autowired
	RoomService roomService;

	@PostMapping("/add")
	public ResponseEntity<String> addAllRooms(@Valid @RequestBody List<RoomDTO> roomDTOs) {
		String message = roomService.addRooms(roomDTOs);
		return ResponseEntity.ok(message);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateRooms(@Valid @RequestBody List<RoomDTO> roomDTOs) {
		String message = roomService.updateRooms(roomDTOs);
		return ResponseEntity.ok(message);
	}

	@GetMapping("/available")
	public ResponseEntity<List<RoomDTO>> getAvailableRooms(@Valid @RequestParam(required = false) Integer roomId,
			@Valid @RequestParam Long hostelId) throws Exception {
		List<RoomDTO> rooms = roomService.getAllAvailableRooms(roomId, hostelId);
		return ResponseEntity.ok(rooms);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteRoom(@Valid @RequestParam(required = false) Integer roomId,
			@Valid @RequestParam Long hostelId) throws Exception {
		String message = roomService.deleteRoomByRoomId(roomId, hostelId);
		return ResponseEntity.ok(message);
	}

	@GetMapping("/test")
	public String test() {
		return "tested";
	}

	/**
	 * @PutMapping("/update/hostelName") public ResponseEntity<String>
	 * updateRooms(@RequestBody Map<String, String> hostelMap) { String message =
	 * roomService.updateHostels(hostelMap); return ResponseEntity.ok(message); }
	 **/
	/**
	 * @GetMapping("/hostel") public ResponseEntity<List<RoomDTO>>
	 * getAllRoomsByHostel(@RequestParam String hostelName) { List<RoomDTO> rooms =
	 * roomService.getAllRoomsByHostel(hostelName); return ResponseEntity.ok(rooms);
	 * }
	 **/
}
