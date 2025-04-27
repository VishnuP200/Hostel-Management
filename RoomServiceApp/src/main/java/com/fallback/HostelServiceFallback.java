package com.fallback;



import org.springframework.stereotype.Component;
import com.feignClient.HostelClient;

@Component
public class HostelServiceFallback implements HostelClient {

	@Override
	public boolean hostelExisits(Long hostelId) {
		System.out.println("In fallBack ---" + hostelId);
		return false;
	}
}
