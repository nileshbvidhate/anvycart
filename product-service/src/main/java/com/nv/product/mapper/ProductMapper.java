package com.nv.product.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.nv.product.dto.CategoryResponse;
import com.nv.product.dto.ImageResponse;
import com.nv.product.dto.InternalProductResponse;
import com.nv.product.dto.ProductResponse;
import com.nv.product.dto.StockResponse;
import com.nv.product.entity.Product;

public class ProductMapper {

	@Value("${gateway.base-url}")
	private static String baseUrl;
	
	public static BigDecimal calculateFinalPrice(BigDecimal basePrice, Integer discountPercentage) {

		if (discountPercentage == null || discountPercentage <= 0) {
			return basePrice.setScale(2, RoundingMode.HALF_UP);
		}

		if (discountPercentage >= 100) {
			return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		}

		BigDecimal discountRate = BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100), 4,
				RoundingMode.HALF_UP);

		return basePrice.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
	}

	public static ProductResponse toProductResponse(Product product, CategoryResponse category,
			List<ImageResponse> images, StockResponse stock) {

		ProductResponse response = new ProductResponse();

		BigDecimal finalPrice = calculateFinalPrice(product.getBasePrice(), product.getDiscountPercentage());

		response.setId(product.getId());
		response.setName(product.getName());
		response.setTitle(product.getTitle());
		response.setBrand(product.getBrand());
		response.setDescription(product.getDescription());
		response.setBasePrice(product.getBasePrice());
		response.setDiscountPercentage(product.getDiscountPercentage());
		response.setFinalPrice(finalPrice);
		response.setStatus(product.getStatus().name()); // enum converting to string
		response.setCategory(category);
		response.setImages(images);
		response.setStock(stock);
		response.setCreatedAt(product.getCreatedAt());
		response.setUpdatedAt(product.getUpdatedAt());

		return response;
	}

	public static InternalProductResponse toProductInternalResponse(Product product, Integer availableQuantity,
			String imageUrl) {

		BigDecimal finalPrice = calculateFinalPrice(product.getBasePrice(), product.getDiscountPercentage());

		InternalProductResponse response = new InternalProductResponse();

		response.setProductId(product.getId());
		response.setTitle(product.getTitle());
		response.setName(product.getName());
		response.setBrand(product.getBrand());
		response.setBasePrice(product.getBasePrice());
		response.setDiscountPercentage(product.getDiscountPercentage());
		response.setFinalPrice(finalPrice);
		response.setActive(true); 
		response.setCategoryName(product.getCategory().getName());
		response.setPrimaryImageUrl(baseUrl+imageUrl);
		response.setAvailableQuantity(availableQuantity);

		return response;
	}

}
