package com.nv.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.nv.order.dto.request.InventoryConfirmRequest;
import com.nv.order.dto.request.InventoryReleaseRequest;
import com.nv.order.dto.request.InventoryReserveRequest;
import com.nv.order.dto.response.ApiResponse;

@FeignClient(name = "inventory-service", url = "${services.inventory-service.url}")
public interface InventoryFeignClient {

	@PatchMapping("/{productId}/reserve")
	ApiResponse<Void> reserveStock(@PathVariable Long productId, @RequestBody InventoryReserveRequest request);
	
	@PatchMapping("/{productId}/confirm")
	ApiResponse<Void> confirmStock(@PathVariable Long productId, @RequestBody InventoryConfirmRequest request);
	
	@PatchMapping("/{productId}/release")
	ApiResponse<Void> releaseStock(@PathVariable Long productId, @RequestBody InventoryReleaseRequest request);
}