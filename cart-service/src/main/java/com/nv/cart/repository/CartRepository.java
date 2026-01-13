package com.nv.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nv.cart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	public Optional<Cart> findByUserId(Long userId);
}
