package com.nv.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.nv.product.dto.CategoryResponse;
import com.nv.product.entity.Category;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryResponse> getAllCategories() {

		return categoryRepository.findAll().stream().map(c -> new CategoryResponse(c.getId(), c.getName())).toList();
	}

	public CategoryResponse getCategoryById(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

		return new CategoryResponse(category.getId(), category.getName());
	}

}
