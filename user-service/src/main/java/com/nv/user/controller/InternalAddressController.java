package com.nv.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.user.dto.ApiResponse;
import com.nv.user.dto.InternalAddressResponse;
import com.nv.user.service.AddressService;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/internal/users/addresses")
public class InternalAddressController {

	@Autowired
	private AddressService addressService;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<List<InternalAddressResponse>>> getInternalUserAddresses(
			@PathVariable @NotNull(message = "userId should not be null") Long userId) {

		List<InternalAddressResponse> response = addressService.getInternalUserAddresses(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Internal addresses fetched successfully", response));
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<ApiResponse<InternalAddressResponse>> getAddressById(
			@PathVariable @NotNull(message = "addressId should not be null") Long addressId) {

		InternalAddressResponse response = addressService.getAddressById(addressId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Internal address fetched successfully", response));
	}

	@GetMapping("/{addressId}/validate")
	public ResponseEntity<ApiResponse<Void>> validateAddress(@PathVariable @NotNull Long addressId,
			@RequestHeader("X-User-Id") Long userId) {

		addressService.validateAddress(addressId, userId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Internal address exists", null));
	}
}