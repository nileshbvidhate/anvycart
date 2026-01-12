package com.nv.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String SECRET_KEY;

	@Value("${jwt.expiration}")
	private long EXPIRATION_TIME;

	public String generateToken(Authentication authentication, Long userId) {

		String username = authentication.getName();
		String role = authentication.getAuthorities()
				.iterator()
				.next()
				.getAuthority()
				.replace("ROLE_", "");

		return Jwts.builder()
				.setSubject(username)
				.claim("userId", userId)
				.claim("roles", List.of(role))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(
						Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), 
						SignatureAlgorithm.HS256
						)
				.compact();
	}
}
