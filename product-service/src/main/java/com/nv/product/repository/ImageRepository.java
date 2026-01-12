package com.nv.product.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nv.product.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	
	List<Image> findByProductId(Long productId);
	
	//imageRepository.findByProduct_Id(product.getId()); Both are same
	
	Image findByProductIdAndIsPrimaryTrue(Long productId);
	
	Optional<Image> findByIdAndProductId(Long imageId, Long productId);

	Image findFirstByProductIdOrderByIdAsc(Long productId);

}
