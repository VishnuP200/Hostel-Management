package com.exception;

import java.util.List;

public class ResourceNotFoundException extends RuntimeException {

	private List<errordtls> errors;

	public ResourceNotFoundException(List<errordtls> errors) {
		this.errors = errors;
	}

	public List<errordtls> getErrors() {
		return errors;
	}

	public void setErrors(List<errordtls> errors) {
		this.errors = errors;
	}

}
