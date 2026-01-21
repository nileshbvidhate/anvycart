package com.nv.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nv.order.dto.response.ApiResponse;

@FeignClient(name = "user-service", url = "${services.user-service.url}")
public interface UserFeignClient {

	@GetMapping("/addresses/{addressId}/validate")
	ApiResponse<Void> validateAddress(@RequestHeader Long userId, @PathVariable Long addressId);
}
