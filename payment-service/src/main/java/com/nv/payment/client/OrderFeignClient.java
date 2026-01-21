package com.nv.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.nv.payment.dto.response.ApiResponse;

@FeignClient(name="order-service", url="${services.order-service.url}")
public interface OrderFeignClient {
	
	@PatchMapping("/{orderId}/payment-success")
	ResponseEntity<ApiResponse<Void>> handlePaymentSuccess(@PathVariable Long orderId);
	
	@PatchMapping("/{orderId}/payment-failed")
	ResponseEntity<ApiResponse<Void>> handlePaymentFailed(@PathVariable Long orderId);
	
	
}
