package com.nv.order.mapper;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nv.order.dto.response.CreateOrderResponse;
import com.nv.order.dto.response.OrderItemResponse;
import com.nv.order.dto.response.OrderResponse;
import com.nv.order.dto.response.RazorpayOrderCreateResponse;
import com.nv.order.entity.Order;

@Component
public class OrderMapper {
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	public CreateOrderResponse toCreateOrderResponse(Order order, RazorpayOrderCreateResponse razorpayOrderCreateResponse) {
		
		return CreateOrderResponse.builder()
				.orderId(order.getId())
				.status(order.getStatus().name())
				.razorpayOrderId(razorpayOrderCreateResponse.getRazorpayOrderId())
				.amount(razorpayOrderCreateResponse.getAmount())
				.currency(razorpayOrderCreateResponse.getCurrency())
				.build();
	}
	
	private List<OrderItemResponse> mapItems(Order order) {
        return order.getOrderItems()
                .stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList();
    }
	
	public OrderResponse toOrderResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .addressId(order.getAddressId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .items(mapItems(order))
                .build();
    }

}
