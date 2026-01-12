package com.nv.product.service;

import org.springframework.data.domain.Page;
import com.nv.product.dto.ProductResponse;

public interface ProductService {

	public Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String direction);

	public ProductResponse getProductById(Long productId);

	public Page<ProductResponse> getProductsByCategory(Long categoryId, int page, int size, String sortBy,
			String direction);

	public Page<ProductResponse> getProductsByBrand(String brand, int page, int size, String sortBy, String direction);

	public Page<ProductResponse> searchProducts(String keyword, int page, int size, String sortBy, String direction);

}
