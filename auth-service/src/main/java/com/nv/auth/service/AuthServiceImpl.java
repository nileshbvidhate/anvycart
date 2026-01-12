package com.nv.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nv.auth.dto.AccountStatus;
import com.nv.auth.dto.PasswordReset;
import com.nv.auth.dto.RegisterRequest;
import com.nv.auth.dto.UserResponse;
import com.nv.auth.dto.UserRole;
import com.nv.auth.entity.AuthUser;
import com.nv.auth.exception.DuplicateEmailException;
import com.nv.auth.exception.ResourceNotFoundException;
import com.nv.auth.mapper.AuthUserMapper;
import com.nv.auth.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthUserMapper authUserMapper;

	public AuthUser getAuthUserByUsername(String name) {

		return userRepository.findByUsername(name).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public void register(RegisterRequest request) {

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new DuplicateEmailException("Email already registered");
		}

		AuthUser user = new AuthUser();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(UserRole.USER);
		user.setStatus(AccountStatus.ACTIVE);
		user.setIsEmailVerified(false);

		userRepository.save(user);

		// Future:
		// sendEmailVerification(user);
	}

	public void changePassword(String username, PasswordReset passwordReset) {

		AuthUser user = getAuthUserByUsername(username);

		if (!passwordEncoder.matches(passwordReset.getCurrentPassword(), user.getPassword())) {
			throw new BadCredentialsException("Current password is incorrect");
		}

		user.setPassword(passwordEncoder.encode(passwordReset.getNewPassword()));

		userRepository.save(user);
	}

	public Page<UserResponse> getAllUsers(Pageable pageable, AccountStatus status, UserRole role) {

		Page<AuthUser> users;

		if (status != null && role != null) {
			users = userRepository.findByStatusAndRole(status, role, pageable);
		} else if (status != null) {
			users = userRepository.findByStatus(status, pageable);
		} else if (role != null) {
			users = userRepository.findByRole(role, pageable);
		} else {
			users = userRepository.findAll(pageable);
		}

		return authUserMapper.toDtoPage(users);
	}

	@Override
	public UserResponse getUserById(Long userId) {

		AuthUser user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return authUserMapper.toDto(user);
	}

	@Override
	public UserResponse updateUserStatus(Long userId, AccountStatus status) {

		AuthUser user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setStatus(status);

		userRepository.save(user);

		return authUserMapper.toDto(user);
	}

	@Override
	public UserResponse updateUserRole(Long userId, UserRole role) {

		AuthUser user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setRole(role);

		userRepository.save(user);

		return authUserMapper.toDto(user);
	}

}
