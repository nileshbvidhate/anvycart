package com.nv.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String brand;
    private String title;
    private String description;
    

    private BigDecimal basePrice;
    private Integer discountPercentage;
    private BigDecimal finalPrice; // calculated as basePrice - discount finalPrice

    private String status; //// ACTIVE / INACTIVE

    private CategoryResponse category; // nested DTO
    private List<ImageResponse> images; // all images

    private StockResponse stock; // actual available stock quantity // fetched from Inventory Service

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
