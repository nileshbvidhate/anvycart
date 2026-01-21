package com.nv.order.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nv.order.dto.response.ApiResponse;
import com.nv.order.dto.response.CartItemResponse;

@FeignClient(name = "cart-service", url = "${services.cart-service.url}")
public interface CartFeignClient {

	@GetMapping("/{userId}")
	public ApiResponse<List<CartItemResponse>> getCart(@PathVariable Long userId);
	
	@DeleteMapping("/{userId}")
	public ApiResponse<Void> clearCart(@PathVariable long userId);
	
}
