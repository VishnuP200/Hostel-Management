package com.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BedDTO {

	@NotNull(message = "Room ID cannot be null.")
	private Integer roomId;
	@NotNull(message = "hostel ID cannot be null.")
	private Long hostelId;

	private String bedId;

	@NotNull(message = "Room Type cannot be null.")
	private String roomType;

	@NotBlank(message = "Status is required")
	@Pattern(regexp = "AVAILABLE|BOOKED", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Status must be one of: AVAILABLE, BOOKED")
	private String status;

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

	public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BedDTO [roomId=" + roomId + ", hostelId=" + hostelId + ", bedId=" + bedId + ", roomType=" + roomType
				+ ", status=" + status + "]";
	}

}
