package com.nv.cart.service;

import org.springframework.stereotype.Service;

import com.nv.cart.dto.InternalCartResponse;
import com.nv.cart.mapper.InternalCartResponseMapper;
import com.nv.cart.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalCartServiceImpl implements InternalCartService {

	private final CartRepository cartRepository;

	private final InternalCartResponseMapper internalCartResponseMapper;

	@Override
	public InternalCartResponse getCartForOrder(Long userId) {

		return cartRepository.findByUserId(userId).map(internalCartResponseMapper::toInternalCartResponse)
				.orElseGet(() -> internalCartResponseMapper.toInternalCartResponse(null));
	}

	@Override
	public void clearCartAfterOrder(Long userId) {

		cartRepository.findByUserId(userId).ifPresent(cart -> {
			cart.getItems().clear();
			cartRepository.save(cart);
		});
	}
}
