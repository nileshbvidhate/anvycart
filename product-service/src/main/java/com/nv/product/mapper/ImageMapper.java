package com.nv.product.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nv.product.dto.ImageResponse;
import com.nv.product.entity.Image;

@Component
public class ImageMapper {
	
	@Value("${gateway.base-url}")
	private String baseUrl;
	
	public ImageResponse toImageResponse(Image img) {
		
		ImageResponse imageResponse = new ImageResponse();
		
		imageResponse.setId(img.getId());
		imageResponse.setImageUrl(baseUrl+img.getImageUrl());
		imageResponse.setIsPrimary(img.getIsPrimary());
		
		return imageResponse;
	}
}
