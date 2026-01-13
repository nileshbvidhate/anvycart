package com.nv.cart.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nv.cart.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GatewayTrustFilter extends OncePerRequestFilter {

	@Value("${gateway.token}")
	private String gatewayToken;

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader("X-Gateway-Token");

		if (token == null || !token.equals(gatewayToken)) {

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpServletResponse.SC_FORBIDDEN, "Forbidden",
					"Gateway call required only", request.getRequestURI());

			response.getWriter().write(objectMapper.writeValueAsString(error));
			response.getWriter().flush();
			return;
		}

		filterChain.doFilter(request, response);
	}
}
