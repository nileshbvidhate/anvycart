package com.nv.product.service;

import org.springframework.web.multipart.MultipartFile;
import com.nv.product.dto.ImageResponse;

public interface AdminImageService {

	// Uploades image on a local machine and store that path in DB.
	public ImageResponse uploadProductImage(Long productId, MultipartFile file, boolean isPrimary);

	// Deletes image from local machine permenantly and url from DB.
	public void deleteProductImage(Long productId, Long imageId);

	public ImageResponse patchImage(Long productId, Long imageId, MultipartFile file, Boolean isPrimary);
}
