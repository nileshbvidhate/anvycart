package com.nv.auth.service;

import com.nv.auth.entity.AuthUser;
import com.nv.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username){

		AuthUser user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities("ROLE_" + user.getRole().name()) // ROLE_ADMIN / ROLE_USER
				.build();
	}
}
