package com.nv.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

	@Bean
	RequestInterceptor feignRequestInterceptor() {
		
		return requestTemplate -> {
			requestTemplate.header("X-Gateway-Token", "internal");
		};
		
	}
}
