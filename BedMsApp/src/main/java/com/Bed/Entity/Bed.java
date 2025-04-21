package com.Bed.Entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "bed")
public class Bed {

	@EmbeddedId
	private RoomBedHostelId Id;

	private String roomType;

	@Enumerated(EnumType.STRING)
	private Status status;

	public RoomBedHostelId getId() {
		return Id;
	}

	public void setId(RoomBedHostelId id) {
		Id = id;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Bed [Id=" + Id + ", roomType=" + roomType + ", status=" + status + "]";
	}

}
