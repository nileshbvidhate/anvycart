package com.nv.product.service;

import org.springframework.stereotype.Service;
import com.nv.product.dto.InternalProductResponse;
import com.nv.product.entity.Image;
import com.nv.product.entity.Product;
import com.nv.product.entity.ProductStatus;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.mapper.ProductMapper;
import com.nv.product.repository.ImageRepository;
import com.nv.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalProductServiceImpl implements InternalProductService {

	private final ProductRepository productRepository;
	private final ImageRepository imageRepository;

	@Override
	public InternalProductResponse getInternalProduct(Long productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		if (product.getStatus() != ProductStatus.ACTIVE) {
			throw new IllegalArgumentException("Product is not active");
		}

		Image primaryImage =
		        imageRepository.findByProductIdAndIsPrimaryTrue(productId);

		if (primaryImage == null) {
		    primaryImage = imageRepository
		            .findFirstByProductIdOrderByIdAsc(productId);
		}

		String imageUrl = primaryImage != null ? primaryImage.getImageUrl() : null;

		Integer availableQuantity = 0; // TODO inventory service call 

		return ProductMapper.toProductInternalResponse(product, availableQuantity, imageUrl);
	}

}
