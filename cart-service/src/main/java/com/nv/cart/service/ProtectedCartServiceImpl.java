package com.nv.cart.service;

import org.springframework.stereotype.Service;
import com.nv.cart.dto.CartResponse;
import com.nv.cart.entity.Cart;
import com.nv.cart.entity.CartItem;
import com.nv.cart.exception.ResourceNotFoundException;
import com.nv.cart.mapper.CartMapper;
import com.nv.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProtectedCartServiceImpl implements ProtectedCartService {

	private final CartRepository cartRepository;

	private final CartMapper cartMapper;

	@Override
	public CartResponse getCart(Long userId) {

		return cartRepository.findByUserId(userId).map(cartMapper::toCartResponse)
				.orElseGet(() -> cartMapper.toCartResponse(null));
	}

	@Override
	public CartResponse addItem(Long userId, Long productId, Integer quantity) {

		Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			return newCart;
		});

		CartItem existingItem = cart.getItems().stream().filter(item -> item.getProductId().equals(productId))
				.findFirst().orElse(null);

		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
		} else {
			CartItem newItem = new CartItem();
			newItem.setProductId(productId);
			newItem.setQuantity(quantity);
			newItem.setCart(cart);
			cart.getItems().add(newItem);
		}

		Cart savedCart = cartRepository.save(cart);
		return cartMapper.toCartResponse(savedCart);
	}

	@Override
	public CartResponse patchItemQuantity(Long userId, Long cartItemId, Integer quantity) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

		CartItem cartItem = cart.getItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

		cartItem.setQuantity(quantity);

		Cart savedCart = cartRepository.save(cart);
		return cartMapper.toCartResponse(savedCart);
	}

	@Override
	public CartResponse removeItem(Long userId, Long cartItemId) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

		CartItem cartItem = cart.getItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

		// imp
		cart.getItems().remove(cartItem);

		Cart savedCart = cartRepository.save(cart);
		return cartMapper.toCartResponse(savedCart);
	}

	@Override
	public CartResponse clearCart(Long userId) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

		// clear the all items
		cart.getItems().clear();

		Cart savedCart = cartRepository.save(cart);
		return cartMapper.toCartResponse(savedCart);
	}

}
