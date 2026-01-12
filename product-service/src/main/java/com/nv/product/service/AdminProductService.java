package com.nv.product.service;

import org.springframework.data.domain.Page;

import com.nv.product.dto.ProductCreateRequest;
import com.nv.product.dto.ProductPatchRequest;
import com.nv.product.dto.ProductResponse;
import com.nv.product.dto.ProductUpdateRequest;

public interface AdminProductService {

	public ProductResponse createProduct(ProductCreateRequest request);

	public ProductResponse updateProduct(Long productId, ProductUpdateRequest request);

	public ProductResponse patchProduct(Long productId, ProductPatchRequest request);

	public Page<ProductResponse> getInactiveProducts(int page, int size, String sortBy, String direction);
}
