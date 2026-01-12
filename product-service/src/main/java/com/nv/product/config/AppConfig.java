package com.nv.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

//	@Value("${inventory.service.base-url}")
//	private String baseUrl;

	@Value("${internal.service.token}")
	private String serviceToken;

//	Specific for InventoryWebClient because we are adding it base url while
//	creating the WebClient instance
//	
//	@Bean
//	WebClient inventoryWebClient() {
//
//		return WebClient.builder()
//				.defaultHeader("X-Service-Token", serviceToken)
//				.baseUrl(baseUrl)
//				.build();
//	}

//	 Generic WebClient Instance can be used by multiple services

	@Bean
	WebClient.Builder inventoryWebClient() {

		return WebClient.builder()
				.defaultHeader("X-Service-Token", serviceToken);
				
	}
}