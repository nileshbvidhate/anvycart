package com.nv.user.service;

import java.util.List;

import com.nv.user.dto.AddressResponse;
import com.nv.user.dto.InternalAddressResponse;
import com.nv.user.dto.PatchAddressRequest;
import com.nv.user.dto.AddressRequest;

public interface AddressService {
	AddressResponse createAddress(Long userId, AddressRequest request);
	
	List<AddressResponse> getMyAddresses(Long userId);
	
	AddressResponse getMyDefaultAddress(Long userId);
	
	AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request);
	
	AddressResponse patchAddress(Long userId, Long addressId, PatchAddressRequest request);
	
	void deleteAddress(Long userId, Long addressId);
	
	// for internal service only
	List<InternalAddressResponse> getInternalUserAddresses(Long userId);
	
	InternalAddressResponse getAddressById(Long addressId);
	
	void validateAddress(Long addressId, Long userId);

}
