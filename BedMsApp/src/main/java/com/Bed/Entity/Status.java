package com.Bed.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

	AVAILABLE, BOOKED;

	@JsonCreator
	public static Status fromString(String value) {
		return Status.valueOf(value.trim().toUpperCase());
	}
}
