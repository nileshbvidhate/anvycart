package com.nv.product.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nv.product.client.InventoryClient;
import com.nv.product.client.dto.InventoryCreateRequest;
import com.nv.product.dto.CategoryResponse;
import com.nv.product.dto.ImageResponse;
import com.nv.product.dto.ProductCreateRequest;
import com.nv.product.dto.ProductPatchRequest;
import com.nv.product.dto.ProductResponse;
import com.nv.product.dto.ProductUpdateRequest;
import com.nv.product.dto.StockResponse;
import com.nv.product.entity.Category;
import com.nv.product.entity.Product;
import com.nv.product.entity.ProductStatus;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.mapper.ImageMapper;
import com.nv.product.mapper.ProductMapper;
import com.nv.product.repository.CategoryRepository;
import com.nv.product.repository.ImageRepository;
import com.nv.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

	private final ProductRepository productRepository;

	private final ImageRepository imageRepository;

	private final CategoryRepository categoryRepository;

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

	public ProductResponse createProduct(ProductCreateRequest request) {

		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		Product product = new Product();

		product.setName(request.getName());
		product.setTitle(request.getTitle());
		product.setBrand(request.getBrand());
		product.setDescription(request.getDescription());

		product.setBasePrice(request.getBasePrice());
		product.setDiscountPercentage(request.getDiscountPercentage());

		product.setStatus(ProductStatus.ACTIVE);

		product.setCategory(category);

		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());

		Product saved = productRepository.save(product);

		// Category DTO
		CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getName());

		// Images empty initially
		List<ImageResponse> images = List.of();

		// Stock placeholder : call the inventory service to create empty inventory
		boolean isInventoryCreated = inventoryClient.createInventory(saved.getId(), new InventoryCreateRequest(0));

		StockResponse stock = null;

		if (isInventoryCreated) {
			// initially inventory is empty
			stock = new StockResponse(0, 0, 0);
		}

		return ProductMapper.toProductResponse(saved, categoryResponse, images, stock);
	}

	public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

		// Full replace
		product.setTitle(request.getTitle());
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setBrand(request.getBrand());

		product.setBasePrice(request.getBasePrice());
		product.setDiscountPercentage(request.getDiscountPercentage());

		product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase())); // converting string to enum
		product.setCategory(category);

		product.setUpdatedAt(LocalDateTime.now());

		Product saved = productRepository.save(product);

		// Category DTO
		CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getName());

		// Images
		List<ImageResponse> images = imageRepository.findByProductId(productId).stream()
				.map(imageMapper::toImageResponse).toList();

		// Stock placeholder // calling inventory
		// service////////////////////////////////////////////////
		StockResponse stock = inventoryClient.getInventory(saved.getId());

		return ProductMapper.toProductResponse(saved, categoryResponse, images, stock);
	}

	public ProductResponse patchProduct(Long productId, ProductPatchRequest request) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		if (request.getTitle() != null)
			product.setTitle(request.getTitle());

		if (request.getName() != null)
			product.setName(request.getName());

		if (request.getDescription() != null)
			product.setDescription(request.getDescription());

		if (request.getBrand() != null)
			product.setBrand(request.getBrand());

		if (request.getBasePrice() != null)
			product.setBasePrice(request.getBasePrice());

		if (request.getDiscountPercentage() != null)
			product.setDiscountPercentage(request.getDiscountPercentage());

		if (request.getStatus() != null)
			product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase())); // converting string to enum

		if (request.getCategoryId() != null) {
			Category category = categoryRepository.findById(request.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
			product.setCategory(category);
		}

		product.setUpdatedAt(LocalDateTime.now());

		Product saved = productRepository.save(product);

		CategoryResponse categoryResponse = new CategoryResponse(saved.getCategory().getId(),
				saved.getCategory().getName());

		List<ImageResponse> images = imageRepository.findByProductId(productId).stream()
				.map(imageMapper::toImageResponse).toList();

		// Inventory Service call // to get the stock
		// details/////////////////////////////////////
		StockResponse stock = inventoryClient.getInventory(saved.getId());

		return ProductMapper.toProductResponse(saved, categoryResponse, images, stock);
	}

	public Page<ProductResponse> getInactiveProducts(int page, int size, String sortBy, String direction) {

		Pageable pageable = createPageable(page, size, sortBy, direction);

		Page<Product> productPage = productRepository.findByStatus(ProductStatus.INACTIVE, pageable);

		return productPage.map(product -> {

			CategoryResponse category = new CategoryResponse(product.getCategory().getId(),
					product.getCategory().getName());

			List<ImageResponse> images = imageRepository.findByProductId(product.getId()).stream()
					.map(imageMapper::toImageResponse).toList();

			// Calling inventory service -> get the stock
			// details/////////////////////////////
			StockResponse stock = inventoryClient.getInventory(product.getId());

			return ProductMapper.toProductResponse(product, category, images, stock);
		});
	}
}
