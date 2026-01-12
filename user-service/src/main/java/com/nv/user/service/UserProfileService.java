package com.nv.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nv.user.dto.InternalUserProfileResponse;
import com.nv.user.dto.PatchUserProfileRequest;
import com.nv.user.dto.UserProfileRequest;
import com.nv.user.dto.UserProfileResponse;

public interface UserProfileService {
	
	public UserProfileResponse getMyProfile(Long userId);
	
	public UserProfileResponse createMyProfile(Long userId, UserProfileRequest userProfileRequest);
	
	UserProfileResponse updateMyProfile(Long userId, UserProfileRequest request);
	
	UserProfileResponse patchMyProfile(Long userId, PatchUserProfileRequest request);
	
	//for admin only
	Page<UserProfileResponse> getAllUsers(Pageable pageable);

	
	UserProfileResponse getUserByUserId(Long userId);

	// for internal services only
	InternalUserProfileResponse getInternalUserProfile(Long userId);

}
