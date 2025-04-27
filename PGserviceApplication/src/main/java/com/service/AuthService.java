package com.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DTO.UserDTO;
import com.Entity.User;
import com.repo.AuthRepo;

@Service
public class AuthService {
	
	@Autowired
	AuthRepo authRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public String addUserDetails(UserDTO UDto) throws Exception {
		User user;
		if(authRepo.findByUserName(UDto.getUserName()) != null) {
			throw new Exception("UserName is already presen"+UDto.getUserName());
		}
		user = modelMapper.map(UDto, User.class);
		user.setPassword(passwordEncoder.encode(UDto.getPassWord()));
		authRepo.save(user);
		return "User added successfully";
		
	}

}
