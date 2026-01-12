package com.nv.product.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProductPatchRequest {

    private String title;
    private String name;
    private String description;
    private String brand;

    private BigDecimal basePrice;
    private Integer discountPercentage;

    private Long categoryId;

    private String status;
}

