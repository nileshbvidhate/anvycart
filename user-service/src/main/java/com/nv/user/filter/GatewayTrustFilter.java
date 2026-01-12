package com.nv.user.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GatewayTrustFilter extends OncePerRequestFilter{

	@Value("${gateway.token}")
	private String gatewayToken;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader("X-Gateway-Token");
		
		if(token == null || !token.equals(gatewayToken))
		{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			
			return ;
		}
		
		filterChain.doFilter(request, response);
	}

}