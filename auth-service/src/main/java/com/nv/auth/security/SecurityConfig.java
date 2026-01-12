package com.nv.auth.security;

import com.nv.auth.exception.CustomAccessDeniedException;
import com.nv.auth.exception.JwtAuthenticationEntryPoint;
import com.nv.auth.service.CustomUserDetailsService;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private CustomAccessDeniedException customAccessDeniedHandler;
	
	@Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secretKey;

	// 1 Security Filter Chain
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/auth/login", "/auth/register","/error", "internal/auth/users/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        // Security exceptions
	        .exceptionHandling(exception -> exception
	            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            .accessDeniedHandler(customAccessDeniedHandler)
	        )

	        // JWT validation
	        .oauth2ResourceServer(oauth2 -> oauth2
	        	.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
	        )
	        
	        .formLogin(form -> form.disable())
	        .httpBasic(basic -> basic.disable());

	    return http.build();
	}

	@Bean
    JwtDecoder jwtDecoder() {
        
		SecretKey key =
            new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(key).build();
    }

	// 2 Password Encoder (industry standard)
	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//    In Spring Boot 3 / Spring Security 6, you don’t need to define a separate DaoAuthenticationProvider bean if you are already creating the AuthenticationManager bean using AuthenticationManagerBuilder and setting the userDetailsService + passwordEncoder.
//	@Bean
//	DaoAuthenticationProvider authenticationProvider() {
//
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//		provider.setPasswordEncoder(passwordEncoder());
//
//		return provider;
//	}

	// 3 Authentication Manager Bean
	@Bean
	 AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		authBuilder
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());

		return authBuilder.build();
	}
	
	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {

	    JwtGrantedAuthoritiesConverter converter =
	            new JwtGrantedAuthoritiesConverter();

	    converter.setAuthoritiesClaimName("roles"); // JWT claim
	    converter.setAuthorityPrefix("ROLE_");

	    JwtAuthenticationConverter jwtAuthConverter =
	            new JwtAuthenticationConverter();

	    jwtAuthConverter.setJwtGrantedAuthoritiesConverter(converter);

	    return jwtAuthConverter;
	}

}
