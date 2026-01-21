package com.nv.user.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nv.user.dto.AddressResponse;
import com.nv.user.dto.InternalAddressResponse;
import com.nv.user.dto.PatchAddressRequest;
import com.nv.user.dto.AddressRequest;
import com.nv.user.entity.Address;
import com.nv.user.exception.BadRequestException;
import com.nv.user.exception.ResourceNotFoundException;
import com.nv.user.mapper.AddressMapper;
import com.nv.user.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private AddressMapper addressMapper;

	@Override
	public AddressResponse createAddress(Long userId, AddressRequest request) {

		// If new address is default → unset old default
		if (request.getIsDefault()) {
			addressRepo.findByUserIdAndIsDefaultTrue(userId).ifPresent(addr -> {
				addr.setIsDefault(false);
				addressRepo.save(addr);
			});
		}

		Address address = new Address();
		address.setUserId(userId);
		address.setAddressLine1(request.getAddressLine1());
		address.setAddressLine2(request.getAddressLine2());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPostalCode(request.getPostalCode());
		address.setCountry(request.getCountry());
		address.setIsDefault(request.getIsDefault());

		Address saved = addressRepo.save(address);

		return addressMapper.toDto(saved);
	}

	@Override
	public List<AddressResponse> getMyAddresses(Long userId) {

		List<Address> addresses = addressRepo.findByUserId(userId);

		return addresses.stream().map(addressMapper::toDto).toList();
	}

	@Override
	public AddressResponse getMyDefaultAddress(Long userId) {

		Address address = addressRepo.findByUserIdAndIsDefaultTrue(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Default address not found"));

		return addressMapper.toDto(address);
	}

	@Override
	public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {

		Address address = addressRepo.findByIdAndUserId(addressId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		// Handle default logic
		if (request.getIsDefault()) {
			addressRepo.findByUserIdAndIsDefaultTrue(userId).ifPresent(a -> {
				if (!a.getId().equals(addressId)) {
					a.setIsDefault(false);
					addressRepo.save(a);
				}
			});
		}

		address.setAddressLine1(request.getAddressLine1());
		address.setAddressLine2(request.getAddressLine2());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPostalCode(request.getPostalCode());
		address.setCountry(request.getCountry());
		address.setIsDefault(request.getIsDefault());

		Address updated = addressRepo.save(address);

		return addressMapper.toDto(updated);
	}

	@Override
	public AddressResponse patchAddress(Long userId, Long addressId, PatchAddressRequest request) {

		Address address = addressRepo.findByIdAndUserId(addressId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		if (request.getAddressLine1() != null)
			address.setAddressLine1(request.getAddressLine1());

		if (request.getAddressLine2() != null)
			address.setAddressLine2(request.getAddressLine2());

		if (request.getCity() != null)
			address.setCity(request.getCity());

		if (request.getState() != null)
			address.setState(request.getState());

		if (request.getPostalCode() != null)
			address.setPostalCode(request.getPostalCode());

		if (request.getCountry() != null)
			address.setCountry(request.getCountry());

		if (request.getIsDefault() != null) {

			if (request.getIsDefault()) {
				addressRepo.findByUserIdAndIsDefaultTrue(userId).ifPresent(a -> {
					if (!a.getId().equals(addressId)) {
						a.setIsDefault(false);
						addressRepo.save(a);
					}
				});
			}

			address.setIsDefault(request.getIsDefault());
		}

		Address updated = addressRepo.save(address);

		return addressMapper.toDto(updated);
	}

	@Override
	public void deleteAddress(Long userId, Long addressId) {

		Address address = addressRepo.findByIdAndUserId(addressId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		addressRepo.delete(address);
	}

	@Override
	public List<InternalAddressResponse> getInternalUserAddresses(Long userId) {

		List<Address> addresses = addressRepo.findByUserId(userId);

		if (addresses.isEmpty()) {
			throw new ResourceNotFoundException("No addresses found for user");
		}

		return addresses.stream()
				.map(addr -> new InternalAddressResponse(addr.getId(), addr.getUserId(), addr.getAddressLine1(),
						addr.getAddressLine2(), addr.getCity(), addr.getState(), addr.getPostalCode(),
						addr.getCountry(), addr.getIsDefault()))
				.toList();
	}

	@Override
	public InternalAddressResponse getAddressById(Long addressId) {

		Address addr = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		return new InternalAddressResponse(addr.getId(), addr.getUserId(), addr.getAddressLine1(),
				addr.getAddressLine2(), addr.getCity(), addr.getState(), addr.getPostalCode(), addr.getCountry(),
				addr.getIsDefault());
	}

	@Override
	public void validateAddress(Long addressId, Long userId) {

	    Address addr = addressRepo.findById(addressId)
	            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

	    if (!addr.getUserId().equals(userId)) {
	        throw new BadRequestException("Address does not belong to user");
	    }
	}


}
