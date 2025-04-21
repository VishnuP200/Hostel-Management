package com.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RoomId implements Serializable {

	private Integer roomId;

	@Column(name = "hostel_Id")
	private Long hostelId;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	@Override
	public String toString() {
		return "RoomId [roomId=" + roomId + ", hostelId=" + hostelId + "]";
	}

	public RoomId(Integer roomId, Long hostelId) {
		super();
		this.roomId = roomId;
		this.hostelId = hostelId;
	}

	public Long getHostelId() {
		return hostelId;
	}

	public void setHostelId(Long hostelId) {
		this.hostelId = hostelId;
	}

	public RoomId() {

	}

}
