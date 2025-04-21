package com.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Bed.Entity.Bed;
import com.Bed.Entity.RoomBedHostelId;
import com.Bed.Entity.Status;

@Repository
public interface BedRepository extends JpaRepository<Bed, RoomBedHostelId> {

	@Query("SELECT COUNT(b) FROM Bed b WHERE b.id.hostelId = :hostelId AND b.id.roomId = :roomId")
	Integer countBedsByHostelIdAndRoomId(Long hostelId, Integer roomId);

	@Query("SELECT b FROM Bed b WHERE b.id.roomId = :roomId AND b.id.hostelId = :hostelId AND b.status = :status")
	List<Bed> findByRoomIdAndHostelIdAndStatus(Integer roomId, Long hostelId, Status status);

	@Query("SELECT b FROM Bed b WHERE b.id.hostelId = :hostelId AND b.status = :status")
	List<Bed> findAllBedsByhostelId(Long hostelId, Status status);

	@Query("SELECT b FROM Bed b WHERE b.id.roomId = :roomId AND b.id.hostelId = :hostelId")
	List<Bed> findByRoomIdAndHostelId(Integer roomId, Long hostelId);
}
