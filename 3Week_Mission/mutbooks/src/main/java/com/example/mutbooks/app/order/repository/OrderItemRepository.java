package com.example.mutbooks.app.order.repository;

import com.example.mutbooks.app.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByPayDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
