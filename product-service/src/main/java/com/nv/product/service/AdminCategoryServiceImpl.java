package com.nv.product.service;

import org.springframework.stereotype.Service;
import com.nv.product.dto.CategoryCreateRequest;
import com.nv.product.dto.CategoryResponse;
import com.nv.product.entity.Category;
import com.nv.product.exception.ResourceAlreadyExistsException;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.repository.CategoryRepository;
import com.nv.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

	private final CategoryRepository categoryRepository;

	private final ProductRepository productRepository;

	@Override
	@Transactional
	public CategoryResponse createCategory(CategoryCreateRequest request) {

		if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
			throw new ResourceAlreadyExistsException("Category already exists with name: " + request.getName());
		}

		Category category = new Category();
		category.setName(request.getName());

		Category saved = categoryRepository.save(category);

		return new CategoryResponse(saved.getId(), saved.getName());
	}

	@Override
	@Transactional
	public CategoryResponse patchCategory(Long categoryId, CategoryCreateRequest request) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

		// Only update if value is present
		if (request.getName() != null && !request.getName().isBlank()) {

			if (categoryRepository.existsByNameIgnoreCase(request.getName())
					&& !category.getName().equalsIgnoreCase(request.getName())) {

				throw new ResourceAlreadyExistsException("Category already exists with name: " + request.getName());
			}

			category.setName(request.getName());
		}

		Category saved = categoryRepository.save(category);

		return new CategoryResponse(saved.getId(), saved.getName());
	}

	@Override
	@Transactional
	public void deleteCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

		// Business rule: category cannot be deleted if products exist
		if (productRepository.existsByCategoryId(categoryId)) {
			throw new IllegalArgumentException("Category cannot be deleted because products are associated with it");
		}

		categoryRepository.delete(category);
	}

}
