package com.PGServiceexample.PGserviceApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com"})
@EnableJpaRepositories(basePackages = "com.repo")
@EntityScan(basePackages = "com.Entity")
@EnableFeignClients(basePackages = "com.feignClient")
@EnableDiscoveryClient
public class PGserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PGserviceApplication.class, args);
		System.out.print("true");
		
	}

}
