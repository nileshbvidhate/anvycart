package com.nv.product.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nv.product.client.InventoryClient;
import com.nv.product.dto.CategoryResponse;
import com.nv.product.dto.ImageResponse;
import com.nv.product.dto.ProductResponse;
import com.nv.product.dto.StockResponse;
import com.nv.product.entity.Product;
import com.nv.product.entity.ProductStatus;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.mapper.ImageMapper;
import com.nv.product.mapper.ProductMapper;
import com.nv.product.repository.ImageRepository;
import com.nv.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ImageRepository imageRepository;

	private final InventoryClient inventoryClient;

	private final ImageMapper imageMapper;

	// Helper method
	private Pageable createPageable(int page, int size, String sortBy, String direction) {

		if (sortBy != null && !sortBy.isBlank()) {
			Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

			return PageRequest.of(page, size, sort);
		}

		return PageRequest.of(page, size);
	}

	public Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String direction) {

		Pageable pageable = createPageable(page, size, sortBy, direction);

		Page<Product> productPage = productRepository.findByStatus(ProductStatus.ACTIVE, pageable);

		return productPage.map(product -> {

			// Category mapping
			CategoryResponse category = new CategoryResponse(product.getCategory().getId(),
					product.getCategory().getName());

			// Image mapping
			List<ImageResponse> images = imageRepository.findByProductId(product.getId()).stream()
					.map(imageMapper::toImageResponse).toList();

			// Stock mapping (inventory call)
			// Calling Inventory Service to get the stcok details
			StockResponse stock = inventoryClient.getInventory(product.getId());

			return ProductMapper.toProductResponse(product, category, images, stock);
		});

	}

	public ProductResponse getProductById(Long productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		// Category
		CategoryResponse category = new CategoryResponse(product.getCategory().getId(),
				product.getCategory().getName());

		// Images
		List<ImageResponse> images = imageRepository.findByProductId(productId).stream()
				.map(imageMapper::toImageResponse).toList();

		// Stock mapping (inventory call)
		// Calling Inventory Service to get the stcok details
		StockResponse stock = inventoryClient.getInventory(product.getId());

		return ProductMapper.toProductResponse(product, category, images, stock);
	}

	public Page<ProductResponse> getProductsByCategory(Long categoryId, int page, int size, String sortBy,
			String direction) {

		Pageable pageable = createPageable(page, size, sortBy, direction);

		Page<Product> productPage = productRepository.findByCategoryIdAndStatus(categoryId, ProductStatus.ACTIVE,
				pageable);

		return productPage.map(product -> {

			// Category mapping
			CategoryResponse category = new CategoryResponse(product.getCategory().getId(),
					product.getCategory().getName());

			// Images
			List<ImageResponse> images = imageRepository.findByProductId(product.getId()).stream()
					.map(imageMapper::toImageResponse).toList();

			// Stock mapping (inventory call)
			// Calling Inventory Service to get the stcok details
			StockResponse stock = inventoryClient.getInventory(product.getId());

			return ProductMapper.toProductResponse(product, category, images, stock);
		});
	}

	public Page<ProductResponse> getProductsByBrand(String brand, int page, int size, String sortBy, String direction) {

		Pageable pageable = createPageable(page, size, sortBy, direction);

		Page<Product> productPage = productRepository.findByBrandIgnoreCaseAndStatus(brand, ProductStatus.ACTIVE,
				pageable);

		return productPage.map(product -> {

			// Category
			CategoryResponse category = new CategoryResponse(product.getCategory().getId(),
					product.getCategory().getName());

			// Images (lazy + image repo)
			List<ImageResponse> images = imageRepository.findByProductId(product.getId()).stream()
					.map(imageMapper::toImageResponse).toList();

			// Stock mapping (inventory call)
			// Calling Inventory Service to get the stcok details
			StockResponse stock = inventoryClient.getInventory(product.getId());

			return ProductMapper.toProductResponse(product, category, images, stock);
		});
	}

	public Page<ProductResponse> searchProducts(String keyword, int page, int size, String sortBy, String direction) {

		Pageable pageable = createPageable(page, size, sortBy, direction);

		Page<Product> productPage = productRepository.searchProducts(keyword, pageable);

		return productPage.map(product -> {

			CategoryResponse category = new CategoryResponse(product.getCategory().getId(),
					product.getCategory().getName());

			List<ImageResponse> images = imageRepository.findByProductId(product.getId()).stream()
					.map(imageMapper::toImageResponse).toList();

			// Stock mapping (inventory call)
			// Calling Inventory Service to get the stcok details
			StockResponse stock = inventoryClient.getInventory(product.getId());

			return ProductMapper.toProductResponse(product, category, images, stock);
		});
	}

}
