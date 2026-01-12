package com.nv.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nv.user.dto.ApiResponse;
import com.nv.user.dto.InternalAddressResponse;
import com.nv.user.service.AddressService;

@RestController
@RequestMapping("/internal/users/addresses")
public class InternalAddressController {

	@Autowired
	private AddressService addressService;

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<List<InternalAddressResponse>>> getInternalUserAddresses(
			@PathVariable Long userId) {

		List<InternalAddressResponse> response = addressService.getInternalUserAddresses(userId);

		return ResponseEntity
				.ok(new ApiResponse<>(HttpStatus.OK.value(), "Internal addresses fetched successfully", response));
	}
}