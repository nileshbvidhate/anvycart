package com.nv.product.service;

import com.nv.product.dto.InternalProductResponse;

public interface InternalProductService {
	
	public InternalProductResponse getInternalProduct(Long productId);
}
