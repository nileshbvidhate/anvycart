package com.nv.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nv.auth.dto.InternalUserResponse;
import com.nv.auth.entity.AuthUser;
import com.nv.auth.exception.ResourceNotFoundException;
import com.nv.auth.repository.UserRepository;

@Service
public class InternalServiceImpl implements InternalService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public InternalUserResponse getUserByEmail(String email) {
		
		 AuthUser user = userRepository.findByUsername(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		 
		 InternalUserResponse response = new InternalUserResponse();
		 
	        response.setId(user.getId());
	        response.setUsername(user.getUsername());
	        response.setRole(user.getRole());
	        response.setStatus(user.getStatus());
	        response.setIsEmailVerified(user.getIsEmailVerified());
	        
		return response;
	}

}
