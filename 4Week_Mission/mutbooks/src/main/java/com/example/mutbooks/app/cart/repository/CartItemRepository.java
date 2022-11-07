package com.example.mutbooks.app.cart.repository;

import com.example.mutbooks.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBuyerIdAndProductId(Long buyerId, Long productId);

    List<CartItem> findAllByBuyerIdOrderByIdDesc(Long buyerId);

    List<CartItem> findByBuyerIdAndIdInOrderByIdDesc(Long buyerId, List<Long> cartItemIds);

    void deleteAllByBuyerIdAndIdIn(Long buyerId, List<Long> cartItemIds);
}
