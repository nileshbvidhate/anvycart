package com.nv.user.mapper;

import org.springframework.stereotype.Component;

import com.nv.user.dto.UserProfileResponse;
import com.nv.user.entity.UserProfile;

@Component
public class UserProfileMapper {
	
	public UserProfileResponse toDto(UserProfile user)
	{
		UserProfileResponse dto = new UserProfileResponse();
		
		dto.setId(user.getId());
		dto.setUserId(user.getUserId());
		dto.setName(user.getName());
		dto.setGender(user.getGender());
		dto.setMobileNumber(user.getMobileNumber());
		dto.setAltMobileNumber(user.getAltMobileNumber());
		dto.setIsMobileVerified(user.getIsMobileVerified());

		return dto;
	}
}
