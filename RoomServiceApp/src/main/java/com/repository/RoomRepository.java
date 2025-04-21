package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entity.Room;
import com.entity.RoomId;
import com.entity.Status;

import jakarta.transaction.Transactional;

public interface RoomRepository extends JpaRepository<Room, RoomId> {

	List<Room> findByRoomId_HostelIdAndStatus(Long hostelId, Status status);

	@Query("SELECT MAX(r.roomId.roomId) FROM Room r WHERE r.roomId.hostelId = :hostelId")
	Optional<Integer> findMaxRoomIdByHostelId(@Param("hostelId") Long hostelId);

	boolean existsByRoomId_HostelId(Long hostelId);

	@Query("SELECT r FROM Room r WHERE r.roomId.roomId = :roomId AND r.roomId.hostelId = :hostelId")
	Optional<Room> findRoomByRoomIdAndHostelId(@Param("roomId") int roomId, @Param("hostelId") Long hostelId);

	List<Room> findByRoomId_HostelId(Long hostelId);

	@Query("SELECT COUNT(*) > 0 FROM Room r WHERE r.roomId.roomId = :roomId AND r.roomId.hostelId = :hostelId")
	boolean exisitsByRoomId_RoomIdAndRoomId_HostelId(@Param("roomId") Integer roomId, @Param("hostelId") Long hostelId);

//	@Transactional
//	@Modifying
//	@Query("UPDATE Room r SET r.roomId.hostelName = :newHostelName WHERE r.roomId.hostelName = :oldHostelName")
//	void updateHostelNameInRooms(String oldHostelName, String newHostelName);

}
