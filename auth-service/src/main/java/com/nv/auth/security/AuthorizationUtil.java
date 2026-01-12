package com.nv.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthorizationUtil {

	@Value("${gateway.token}")
	private String gatewayToken;

	public void requireGateway(HttpServletRequest request) {

		String token = request.getHeader("X-Gateway-Token");

		if (token == null || !token.equals(gatewayToken)) {
			
			throw new AccessDeniedException("Gateway call required only");
		}
	}
}
