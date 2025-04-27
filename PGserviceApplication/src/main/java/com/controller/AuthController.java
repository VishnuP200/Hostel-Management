package com.controller;

import org.apache.hc.client5.http.auth.AuthScheme;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.DTO.UserDTO;
import com.Entity.User;
import com.repo.AuthRepo;
import com.service.AuthService;
import com.util.JWTUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	AuthRepo authRepo;
	
	@PostMapping("/generate")
	public String generateAuthToken(@RequestBody UserDTO userDto) {
		User user = authRepo.findByUserName(userDto.getUserName());
		if(user != null && userDto.getPassWord() != null 
				&& passwordEncoder.matches(userDto.getPassWord(), user.getPassword())){
			String token =  jwtUtil.generateAuthToken(userDto.getUserName());
			JSONObject obj = new JSONObject();
			obj.put("authtoken", token);
			return obj.toString();
		}else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}
		
	}
	
	@PostMapping("/add")
	public String addUser(@Valid @RequestBody UserDTO uDTo) throws Exception {
		System.out.println("user is" + uDTo);
		return authService.addUserDetails(uDTo);
	}

}
