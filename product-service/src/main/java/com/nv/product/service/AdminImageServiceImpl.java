package com.nv.product.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.nv.product.dto.ImageResponse;
import com.nv.product.entity.Image;
import com.nv.product.entity.Product;
import com.nv.product.exception.ResourceNotFoundException;
import com.nv.product.mapper.ImageMapper;
import com.nv.product.repository.ImageRepository;
import com.nv.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminImageServiceImpl implements AdminImageService {

	private final ProductRepository productRepository;
	private final ImageRepository imageRepository;

	private final ImageMapper imageMapper;

	// Injecting folder path from YAML
	@Value("${product.image.upload-dir}")
	private String IMAGE_DIR;

	@Transactional
	@Override
	public ImageResponse uploadProductImage(Long productId, MultipartFile file, boolean isPrimary) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("Uploaded file is empty");
		}

		try {

			// Generate unique filename
			String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

			// Save file locally
			Path filePath = Paths.get(IMAGE_DIR, filename);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			String imageUrl = "/images/" + filename;

			// If the new image is primary, unset existing primary image for this product
			if (isPrimary) {
				Image oldPrimary = imageRepository.findByProductIdAndIsPrimaryTrue(productId);

				if (oldPrimary != null) {
					oldPrimary.setIsPrimary(false);
					imageRepository.save(oldPrimary);
				}
			}

			// Save Image entity
			Image image = new Image();

			image.setProduct(product);
			image.setImageUrl(imageUrl);
			image.setIsPrimary(isPrimary);

			Image saved = imageRepository.save(image);

			return imageMapper.toImageResponse(saved);

		} catch (IOException e) {
			throw new RuntimeException("Failed to store image file", e);
		}
	}

	@Transactional
	@Override
	public void deleteProductImage(Long productId, Long imageId) {

		Image image = imageRepository.findByIdAndProductId(imageId, productId)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found"));

		boolean wasPrimary = image.getIsPrimary();

		// Delete file from local folder
		try {
			Path path = Paths.get(IMAGE_DIR, image.getImageUrl().replace("/images/", ""));
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException("Failed to delete image file");
		}

		// Delete DB record
		imageRepository.delete(image);

		// If primary deleted → assign another image as primary
		if (wasPrimary) {
			List<Image> remaining = imageRepository.findByProductId(productId);
			if (!remaining.isEmpty()) {
				Image newPrimary = remaining.get(0);
				newPrimary.setIsPrimary(true);
				imageRepository.save(newPrimary);
			}
		}
	}

	@Transactional
	@Override
	public ImageResponse patchImage(Long productId, Long imageId, MultipartFile file, Boolean isPrimary) {

		Image image = imageRepository.findById(imageId)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found"));

		if (!image.getProduct().getId().equals(productId)) {
			throw new IllegalArgumentException("Image does not belong to this product");
		}

		// Update file if provided
		if (file != null && !file.isEmpty()) {
			
			// DELETE OLD FILE FIRST
	        String oldImagePath = image.getImageUrl();
	        if (oldImagePath != null) {
	            try {
	                Path oldFile = Paths.get(
	                        IMAGE_DIR,
	                        oldImagePath.replace("/images/", "")
	                );
	                Files.deleteIfExists(oldFile);
	            } catch (IOException e) {
	                throw new RuntimeException("Failed to delete old image file",e);
	            }
	        }
			
			String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path path = Paths.get(IMAGE_DIR, filename);

			try {
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new RuntimeException("Failed to save image", e);
			}

			image.setImageUrl("/images/" + filename);
		}

		// Update primary flag if provided
		if (isPrimary != null) {

			if (isPrimary) {
				Image oldPrimary = imageRepository.findByProductIdAndIsPrimaryTrue(productId);

				if (oldPrimary != null && !oldPrimary.getId().equals(imageId)) {
					oldPrimary.setIsPrimary(false);
					imageRepository.save(oldPrimary);
				}
			}

			image.setIsPrimary(isPrimary);
		}

		Image saved = imageRepository.save(image);

		return imageMapper.toImageResponse(saved);
	}
}
