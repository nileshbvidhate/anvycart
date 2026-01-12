package com.nv.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.nv.inventory.dto.ApiResponse;
import com.nv.inventory.dto.ConfirmStockRequest;
import com.nv.inventory.dto.InventoryCreateRequest;
import com.nv.inventory.dto.InternalInventoryResponse;
import com.nv.inventory.dto.ReleaseStockRequest;
import com.nv.inventory.dto.ReserveStockRequest;
import com.nv.inventory.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/inventories")
@Validated
public class InternalInventoryController {

	private final InventoryService inventoryService;

	// Create inventory (internal) Called by Product Service when product is created
	@PostMapping("/{productId}")
	public ResponseEntity<ApiResponse<Void>> createInventory(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId,
			@RequestBody @Valid InventoryCreateRequest request) {

		inventoryService.createInventory(productId,request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Inventory created", null));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<InternalInventoryResponse>> getInventory(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId) {

		InternalInventoryResponse inventory = inventoryService.getInventory(productId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Inventory fetched", inventory));
	}

	@PatchMapping("/{productId}/reserve")
	public ResponseEntity<ApiResponse<Void>> reserveStock(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId,
			@RequestBody @Valid ReserveStockRequest request) {

		inventoryService.reserveStock(productId, request.getQuantity());

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Stock reserved", null));
	}

	@PatchMapping("/{productId}/confirm")
	public ResponseEntity<ApiResponse<Void>> confirmStock(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId,
			@RequestBody @Valid ConfirmStockRequest request) {

		inventoryService.confirmStock(productId, request.getQuantity());

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Stock confirmed", null));
	}

	@PatchMapping("/{productId}/release")
	public ResponseEntity<ApiResponse<Void>> releaseStock(
			@PathVariable @NotNull(message = "ProductId must not be null") Long productId,
			@RequestBody @Valid ReleaseStockRequest request) {

		inventoryService.releaseStock(productId, request.getQuantity());

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Stock released", null));
	}
}
