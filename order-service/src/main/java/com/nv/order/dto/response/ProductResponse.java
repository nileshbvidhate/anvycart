package com.nv.order.dto.response;

import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductResponse {
	
	private Long productId;

    private String name;

    private BigDecimal basePrice;
    private Integer discountPercentage;

    private String status;

}