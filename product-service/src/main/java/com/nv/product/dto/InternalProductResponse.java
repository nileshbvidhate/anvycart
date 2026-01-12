package com.nv.product.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InternalProductResponse {

    private Long productId;

    private String title;
    private String name;
    private String brand;

    private BigDecimal basePrice;
    private Integer discountPercentage;
    private BigDecimal finalPrice;

    private Boolean active;

    private String categoryName;

    private String primaryImageUrl;

    private Integer availableQuantity;
}
