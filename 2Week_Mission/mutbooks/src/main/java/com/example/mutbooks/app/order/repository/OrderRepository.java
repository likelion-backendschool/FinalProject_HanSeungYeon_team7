package com.example.mutbooks.app.order.repository;

import com.example.mutbooks.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
