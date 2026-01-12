package com.nv.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nv.user.dto.InternalUserProfileResponse;
import com.nv.user.dto.PatchUserProfileRequest;
import com.nv.user.dto.UserProfileRequest;
import com.nv.user.dto.UserProfileResponse;
import com.nv.user.entity.UserProfile;
import com.nv.user.exception.ResourceAlreadyExistsException;
import com.nv.user.exception.ResourceNotFoundException;
import com.nv.user.mapper.UserProfileMapper;
import com.nv.user.repository.UserProfileRepository;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileRepository userProfileRepo;

	@Autowired
	private UserProfileMapper userProfileMapper;

	@Override
	public UserProfileResponse getMyProfile(Long userId) {

		UserProfile profile = userProfileRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

		return userProfileMapper.toDto(profile);
	}

	@Override
	public UserProfileResponse createMyProfile(Long userId, UserProfileRequest request) {

		// Check if profile already exists
		if (userProfileRepo.findByUserId(userId).isPresent()) {
			throw new ResourceAlreadyExistsException("User profile already exists");
		}

		// Map DTO to Entity
		UserProfile profile = new UserProfile();
		profile.setUserId(userId);
		profile.setName(request.getName());
		profile.setGender(request.getGender());
		profile.setMobileNumber(request.getMobileNumber());
		profile.setAltMobileNumber(request.getAltMobileNumber());
		profile.setIsMobileVerified(false);

		// Save
		UserProfile savedProfile = userProfileRepo.save(profile);

		// Map back to Response DTO
		return userProfileMapper.toDto(savedProfile);
	}

	@Override
	public UserProfileResponse updateMyProfile(Long userId, UserProfileRequest request) {

		UserProfile profile = userProfileRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

		profile.setName(request.getName());
		profile.setGender(request.getGender());
		profile.setMobileNumber(request.getMobileNumber());
		profile.setAltMobileNumber(request.getAltMobileNumber());

		UserProfile updated = userProfileRepo.save(profile);

		return userProfileMapper.toDto(updated);
	}

	@Override
	public UserProfileResponse patchMyProfile(Long userId, PatchUserProfileRequest request) {

		UserProfile profile = userProfileRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

		if (request.getName() != null) {
			profile.setName(request.getName());
		}

		if (request.getGender() != null) {
			profile.setGender(request.getGender());
		}

		if (request.getMobileNumber() != null) {
			profile.setMobileNumber(request.getMobileNumber());
		}

		if (request.getAltMobileNumber() != null) {
			profile.setAltMobileNumber(request.getAltMobileNumber());
		}

		UserProfile updated = userProfileRepo.save(profile);

		return userProfileMapper.toDto(updated);
	}

	@Override
	public Page<UserProfileResponse> getAllUsers(Pageable pageable) {

		return userProfileRepo.findAll(pageable).map(userProfileMapper::toDto);
	}

	@Override
	public UserProfileResponse getUserByUserId(Long userId) {

		UserProfile profile = userProfileRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

		return userProfileMapper.toDto(profile);
	}

	@Override
	public InternalUserProfileResponse getInternalUserProfile(Long userId) {

		UserProfile profile = userProfileRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User profile not found"));

		InternalUserProfileResponse response = new InternalUserProfileResponse();

		response.setId(profile.getId());
		response.setUserId(profile.getUserId());
		response.setName(profile.getName());
		response.setMobileNumber(profile.getMobileNumber());

		return response;
	}

}
