package com.DTO;

import com.Entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostelDTO {

	@Nullable
	private Long hostelId;

	@NotBlank(message = "Hostel name is required")
	private String hostelName;

	@NotBlank(message = "Location is required")
	private String location;

	@Positive(message = "Price must be greater than 0")
	private double price;

	@Min(value = 1, message = "Capacity must be at least 1")
	private int hostelCapacity;

	@NotNull(message = "Status cannot be null")
	private Status status;

	public String getHostelName() {
		return hostelName;
	}

	public void setHostelName(String hostelName) {
		this.hostelName = hostelName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getHostelCapacity() {
		return hostelCapacity;
	}

	public void setHostelCapacity(int hostelCapacity) {
		this.hostelCapacity = hostelCapacity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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
		return "HostelDTO [hostelId=" + hostelId + ", hostelName=" + hostelName + ", location=" + location + ", price="
				+ price + ", hostelCapacity=" + hostelCapacity + ", status=" + status + "]";
	}

}
