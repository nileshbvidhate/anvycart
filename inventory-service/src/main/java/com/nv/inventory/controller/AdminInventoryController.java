package com.nv.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.inventory.dto.AdjustStockRequest;
import com.nv.inventory.dto.ApiResponse;
import com.nv.inventory.dto.InventoryCreateRequest;
import com.nv.inventory.security.AuthorizationUtil;
import com.nv.inventory.service.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/inventories")
@Validated
public class AdminInventoryController {

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@PatchMapping("/{productId}/adjust")
	public ResponseEntity<ApiResponse<Void>> adjustStock(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId,
			@RequestBody @Valid AdjustStockRequest adjustStockRequest, HttpServletRequest request) {

		authorizationUtil.requireAdmin(request);

		inventoryService.adjustStock(productId, adjustStockRequest.getTotalQuantity());

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Stock adjusted", null));
	}

	@PostMapping("/{productId}")
	public ResponseEntity<ApiResponse<Void>> createInventory(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId,
			@RequestBody @Valid InventoryCreateRequest createInventoryRequest, HttpServletRequest request) {

		authorizationUtil.requireAdmin(request);

		inventoryService.createInventory(productId, createInventoryRequest);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Inventory created", null));
	}
}
