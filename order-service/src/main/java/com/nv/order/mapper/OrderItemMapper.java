package com.nv.order.mapper;

import org.springframework.stereotype.Component;
import com.nv.order.dto.response.OrderItemResponse;
import com.nv.order.entity.OrderItem;

@Component
public class OrderItemMapper {

	public OrderItemResponse toOrderItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .name(item.getName())
                .quantity(item.getQuantity())
                .basePrice(item.getBasePrice())
                .discountPercentage(item.getDiscountPercentage())
                .finalPrice(item.getFinalPrice())
                .build();
    }
}
