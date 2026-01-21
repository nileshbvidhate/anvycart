package com.nv.delivery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nv.delivery.dto.response.ApiResponse;

@FeignClient(name = "order-service", url = "${services.order-service.url}")
public interface OrderFeignClient {

	@PatchMapping("/{orderId}/shipped")
	ResponseEntity<ApiResponse<Void>> markOrderShipped(@PathVariable Long orderId);

	@PatchMapping("/{orderId}/out-for-delivery")
	ResponseEntity<ApiResponse<Void>> markOrderOutForDelivery(@PathVariable Long orderId);

	@PatchMapping("/{orderId}/delivered")
	ResponseEntity<ApiResponse<Void>> markOrderDelivered(@PathVariable Long orderId);

}
