package com.Bed.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RoomBedHostelId {

	private String bedId;

	private Integer roomId;

	private Long hostelId;

	public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Long getHostelId() {
		return hostelId;
	}

	public void setHostelId(Long hostelId) {
		this.hostelId = hostelId;
	}

	@Override
	public String toString() {
		return "RoomBedHostelId [bedId=" + bedId + ", roomId=" + roomId + ", hostelId=" + hostelId + "]";
	}

	public RoomBedHostelId(String bedId, Integer roomId, Long hostelId) {
		this.bedId = bedId;
		this.roomId = roomId;
		this.hostelId = hostelId;
	}

	public RoomBedHostelId(Integer roomID, Long hostelId) {
		this.roomId = roomId;
		this.hostelId = hostelId;
	}

	public RoomBedHostelId() {

	}

}
