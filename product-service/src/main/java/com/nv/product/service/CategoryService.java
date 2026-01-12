package com.nv.product.service;

import java.util.List;
import com.nv.product.dto.CategoryResponse;

public interface CategoryService {
	
	public List<CategoryResponse> getAllCategories();
	
	public CategoryResponse getCategoryById(Long categoryId);
}
