package com.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.Dto.RoomDTO;
import com.entity.Room;

@Configuration
public class modelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<Room, RoomDTO>() {
			@Override
			protected void configure() {
				// Mapping RoomId properties
				map(source.getRoomId().getRoomId(), destination.getRoomId());
				map(source.getRoomId().getHostelId(), destination.getHostelId());
			}
		});

		return modelMapper;
	}

}
