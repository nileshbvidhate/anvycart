package com.nv.product.service;

import org.springframework.stereotype.Service;
import com.nv.product.dto.InternalProductResponse;
import com.nv.product.entity.Product;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.mapper.ProductMapper;
import com.nv.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalProductServiceImpl implements InternalProductService {

	private final ProductRepository productRepository;

	@Override
	public InternalProductResponse getInternalProduct(Long productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		return ProductMapper.toProductInternalResponse(product);
	}

}
