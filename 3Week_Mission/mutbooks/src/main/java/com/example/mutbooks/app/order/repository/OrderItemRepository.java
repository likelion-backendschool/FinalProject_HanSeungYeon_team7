package com.example.mutbooks.app.order.repository;

import com.example.mutbooks.app.order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime startOfDay, LocalDateTime endOfDay);
    Page<OrderItem> findAllByPayDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}
