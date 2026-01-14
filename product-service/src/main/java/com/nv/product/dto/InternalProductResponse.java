package com.nv.product.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InternalProductResponse {
	
	private Long productId;

    private String name;

    private BigDecimal basePrice;
    private Integer discountPercentage;

    private String status;

}
