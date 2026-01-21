package com.nv.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nv.order.dto.request.DeliveryCreateRequest;
import com.nv.order.dto.response.ApiResponse;

@FeignClient(name = "delivery-service", url = "${services.delivery-service.url}")
public interface DeliveryFeignClient {

	@PatchMapping("/{orderId}/cancel")
	ApiResponse<Void> cancelDelivery(@PathVariable Long orderId);
	
	@PostMapping
	ApiResponse<Void> createDelivery(@RequestBody DeliveryCreateRequest deliveryCreateRequest);
}
