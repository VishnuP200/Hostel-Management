package com.roomServiceApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com"})
@EnableJpaRepositories(basePackages = "com.repository")
@EntityScan(basePackages = "com.entity")
@EnableFeignClients(basePackages = "com.feignClient")
public class RoomServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomServiceAppApplication.class, args);
		System.out.print(true);
	
	}

}
