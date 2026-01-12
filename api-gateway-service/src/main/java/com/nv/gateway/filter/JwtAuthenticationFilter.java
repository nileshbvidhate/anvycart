package com.nv.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.nv.gateway.exception.CustomAccessDeniedException;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

	@Autowired
    private JwtUtil jwtUtil;
	
	@Value("${gateway.token}")
	private String gatewayToken;
	
	@Value("${internal.service.token}")
	private String serviceToken;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 1 Auth Service Public endpoints (no JWT)
        if (path.startsWith("/auth/register") || path.startsWith("/auth/login") || path.startsWith("/auth/password/forgot")) {
            return chain.filter(exchange);
        }
        
        // Product Service Public endpoints
        if(path.startsWith("/images") || path.startsWith("/public/products"))
        {
        	ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(request -> request.headers(headers -> {
                        headers.add("X-Gateway-Token", gatewayToken); // trusted gateway identity
                    }))
                    .build();

            return chain.filter(mutatedExchange);
        }
        
        // 2 Inter-service communication // adding gateway token for downstream services
        if (path.startsWith("/internal")) {

            String serviceHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-Service-Token");

            if (serviceHeader == null || !serviceHeader.equals(serviceToken)) {
                throw new CustomAccessDeniedException("Internal service role required");
            }

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(request -> request.headers(headers -> {
                        headers.remove("X-Service-Token"); // never forward raw service token
                        headers.add("X-Gateway-Token", gatewayToken); // trusted gateway identity
                    }))
                    .build();

            return chain.filter(mutatedExchange);
        }

        // 3 For Protected APIS (Read Authorization header)
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing Authorization Header");
        }

        String token = authHeader.substring(7);

        // 4 Validate JWT
        if (!jwtUtil.isTokenValid(token)) {
            throw new SecurityException("Invalid JWT TOKEN / Expired JWT TOKEN");
        }

        // 5 Extract user info from token
        String userId = jwtUtil.extractUserId(token).toString();
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        
        // 6. ROLE BASED ACCESS // access this for only admin role
        if (path.startsWith("/admin/") && !role.equals("ADMIN")) {
        	
            throw new CustomAccessDeniedException("Admin role required");
        }

        
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request -> request.headers(headers -> {
                    headers.add("X-User-Id", userId);
                    headers.add("X-Username", username);
                    headers.add("X-User-Role", role);
                    headers.add("X-Gateway-Token",gatewayToken); // for the trust validation in downstream services
                }))
                .build();

        // 6 Continue routing
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1; // runs early
    }
}
