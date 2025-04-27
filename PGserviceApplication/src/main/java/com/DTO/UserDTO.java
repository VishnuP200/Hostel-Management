package com.DTO;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {
	
	@NotBlank(message = "userName is required")
	private String userName;
	
	@NotBlank(message = "passWord is required")
	private String passWord;
	
	@NotBlank(message = "role is required")
	private String role;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	

}
