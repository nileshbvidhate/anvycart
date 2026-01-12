package com.nv.product.service;

import com.nv.product.dto.CategoryCreateRequest;
import com.nv.product.dto.CategoryResponse;

public interface AdminCategoryService {
	
	CategoryResponse createCategory(CategoryCreateRequest request);

    CategoryResponse patchCategory(Long categoryId, CategoryCreateRequest request);
	
    public void deleteCategory(Long categoryId);
}
