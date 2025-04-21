package com.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BedMsApp")
public interface BedClient {

	@DeleteMapping("/bed/delete")
	public ResponseEntity<String> deleteBeds(@RequestParam(required = false) String bedId, @RequestParam Integer roomId,
			@RequestParam Long hostelId) throws Exception;

}
