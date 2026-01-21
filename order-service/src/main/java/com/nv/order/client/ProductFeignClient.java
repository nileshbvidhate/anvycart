package com.nv.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nv.order.dto.response.ApiResponse;
import com.nv.order.dto.response.ProductResponse;

@FeignClient(name="product-service",url="${services.product-service.url}")
public interface ProductFeignClient {
	
	@GetMapping("/{productId}")
	public ApiResponse<ProductResponse> getProduct(@PathVariable Long productId);
}
