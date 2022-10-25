package com.example.mutbooks.app.cart.repository;

import com.example.mutbooks.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId);

    List<CartItem> findAllByMemberIdOrderByIdDesc(Long memberId);
}
