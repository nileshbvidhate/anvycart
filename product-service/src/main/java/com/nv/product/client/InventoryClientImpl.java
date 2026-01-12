package com.nv.product.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.nv.product.client.dto.InventoryCreateRequest;
import com.nv.product.dto.ApiResponse;
import com.nv.product.dto.StockResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryClientImpl implements InventoryClient {

	private final WebClient.Builder webClientBuilder;
	
	private WebClient inventoryWebClient;

    @Value("${inventory.service.base-url}")
    private String inventoryBaseUrl;

    @PostConstruct
    private void init() {
        this.inventoryWebClient = webClientBuilder
                .baseUrl(inventoryBaseUrl)
                .build();
    }


	@Override
	public boolean createInventory(Long productId, InventoryCreateRequest request) {

		ResponseEntity<Void> response = inventoryWebClient.post()
				.uri("/{productId}", productId)
				.bodyValue(request)
				.retrieve()
				.toBodilessEntity()
				.block();

		return response != null && response.getStatusCode() == HttpStatus.CREATED;
	}
	
	@Override
	public StockResponse getInventory(Long productId) {

	    StockResponse stockResponse = inventoryWebClient.get()
	            .uri("/{productId}", productId)
	            .retrieve()
	            .bodyToMono(new ParameterizedTypeReference<ApiResponse<StockResponse>>() {})
	            .map(ApiResponse::getData)
	            .block();
	    
	    return stockResponse;
	}


}
