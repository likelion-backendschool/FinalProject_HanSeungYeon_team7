package com.example.mutbooks.app.product.repository;

import com.example.mutbooks.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
