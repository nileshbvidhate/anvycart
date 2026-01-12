package com.nv.user.security;

import org.springframework.stereotype.Component;
import com.nv.user.exception.CustomAccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthorizationUtil {
	
	public void requireAdmin(HttpServletRequest request) {
		
		String role = request.getHeader("X-User-Role");

		if (role == null || !role.equals("ADMIN")) {
			throw new CustomAccessDeniedException("Admin role required");
		}
	}
}
