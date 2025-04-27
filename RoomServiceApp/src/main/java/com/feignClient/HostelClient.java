package com.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fallback.HostelServiceFallback;

import jakarta.validation.Valid;

@FeignClient(name = "PGserviceApplication",fallback = HostelServiceFallback.class)
public interface HostelClient {

	@GetMapping("/myapp/api/hostels/exisits")
	public boolean hostelExisits(@RequestParam Long hostelId);

}
