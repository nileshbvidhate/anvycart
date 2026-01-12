package com.nv.user.mapper;

import org.springframework.stereotype.Component;

import com.nv.user.dto.AddressResponse;
import com.nv.user.entity.Address;

@Component
public class AddressMapper {
	
	public AddressResponse toDto(Address address)
	{
		AddressResponse dto = new AddressResponse();
		
		dto.setId(address.getId());
		dto.setUserId(address.getUserId());
		dto.setAddressLine1(address.getAddressLine1());
		dto.setAddressLine2(address.getAddressLine2());
		dto.setCity(address.getCity());
		dto.setPostalCode(address.getPostalCode());
		dto.setState(address.getState());
		dto.setCountry(address.getCountry());
		dto.setIsDefault(address.getIsDefault());

		return dto;
	}

}
