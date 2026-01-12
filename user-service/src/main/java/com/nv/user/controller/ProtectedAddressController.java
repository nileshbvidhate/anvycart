package com.nv.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.user.dto.AddressResponse;
import com.nv.user.dto.ApiResponse;
import com.nv.user.dto.PatchAddressRequest;
import com.nv.user.dto.AddressRequest;
import com.nv.user.service.AddressService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/me")
public class ProtectedAddressController {

	@Autowired
	private AddressService addressService;

	@PostMapping("/addresses")
	public ResponseEntity<ApiResponse<AddressResponse>> createAddress(@RequestHeader("X-User-Id") Long userId,
			@Valid @RequestBody AddressRequest request) {

		AddressResponse response = addressService.createAddress(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(HttpStatus.CREATED.value(), "Address added successfully", response));
	}

	@GetMapping("/addresses")
	public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses(@RequestHeader("X-User-Id") Long userId) {

		List<AddressResponse> response = addressService.getMyAddresses(userId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Addresses fetched successfully", response));
	}

	@GetMapping("/addresses/default")
	public ResponseEntity<ApiResponse<AddressResponse>> getMyDefaultAddress(@RequestHeader("X-User-Id") Long userId) {

		AddressResponse response = addressService.getMyDefaultAddress(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Default address fetched successfully", response));
	}

	@PutMapping("/addresses/{addressId}")
	public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(@RequestHeader("X-User-Id") Long userId,
			@PathVariable Long addressId, @Valid @RequestBody AddressRequest request) {

		AddressResponse response = addressService.updateAddress(userId, addressId, request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Address updated successfully", response));
	}

	@PatchMapping("/addresses/{addressId}")
	public ResponseEntity<ApiResponse<AddressResponse>> patchAddress(@RequestHeader("X-User-Id") Long userId,
			@PathVariable Long addressId, @RequestBody PatchAddressRequest request) {

		AddressResponse response = addressService.patchAddress(userId, addressId, request);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Address updated successfully", response));
	}

	@DeleteMapping("/addresses/{addressId}")
	public ResponseEntity<ApiResponse<Void>> deleteAddress(@RequestHeader("X-User-Id") Long userId,
			@PathVariable Long addressId) {

		addressService.deleteAddress(userId, addressId);

		return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Address deleted successfully", null));
	}

}
