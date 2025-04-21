package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;

import lombok.Data;

@Data
@Entity
@Table(name = "Room")
public class Room {

//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Id
//	private Long Id;

	@EmbeddedId
	private RoomId roomId;

	@Min(value = 1, message = "Room capacity must be at least 1")
	@Column(name = "room_capacity", nullable = false)
	private int roomCapacity;

	@Min(value = 0, message = "Price must be non-negative")
	@Column(nullable = false)
	private double price;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.AVAILABLE;

	public int getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(int roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

//	public Long getId() {
//		return Id;
//	}
//
//	public void setId(Long id) {
//		Id = id;
//	}

	public RoomId getRoomId() {
		return roomId;
	}

	public void setRoomId(RoomId roomId) {
		this.roomId = roomId;
	}

	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", roomCapacity=" + roomCapacity + ", price=" + price + ", status=" + status
				+ "]";
	}

}
