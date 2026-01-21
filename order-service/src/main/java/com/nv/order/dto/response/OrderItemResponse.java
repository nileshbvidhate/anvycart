package com.nv.order.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
	private Long id;
    private Long productId;
    private String name;
    private Integer quantity;
    private BigDecimal basePrice;
    private Integer discountPercentage;
    private BigDecimal finalPrice;
}