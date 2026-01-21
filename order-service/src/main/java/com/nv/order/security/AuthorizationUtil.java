package com.nv.order.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.nv.order.exception.CustomAccessDeniedException;

@Component
public class AuthorizationUtil {

	public void requireAdmin(HttpServletRequest request) {
		String role = request.getHeader("X-User-Role");

		if (role == null || !role.equals("ADMIN")) {
			throw new CustomAccessDeniedException("Admin role required");
		}
	}

}
