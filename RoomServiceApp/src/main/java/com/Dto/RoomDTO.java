package com.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RoomDTO {

	@NotNull(message = "roomId cannot be null")
	private Integer roomId;

	@Min(value = 1, message = "Room capacity must be at least 1")
	private int roomCapacity;

	@Min(value = 100, message = "Price must be non-negative")
	private double price;

	@NotBlank(message = "Status is required")
	@Pattern(regexp = "AVAILABLE|BOOKED", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Status must be one of: AVAILABLE, BOOKED")
	private String status;

	@NotNull(message = "hostelId cannot be null")
	private Long hostelId;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getHostelId() {
		return hostelId;
	}

	public void setHostelId(Long hostelId) {
		this.hostelId = hostelId;
	}

	@Override
	public String toString() {
		return "RoomDTO [roomId=" + roomId + ", roomCapacity=" + roomCapacity + ", price=" + price + ", status="
				+ status + ", hostelId=" + hostelId + "]";
	}

}
