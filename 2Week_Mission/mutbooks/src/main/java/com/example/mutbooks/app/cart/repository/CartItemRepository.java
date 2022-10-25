package com.example.mutbooks.app.cart.repository;

import com.example.mutbooks.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
