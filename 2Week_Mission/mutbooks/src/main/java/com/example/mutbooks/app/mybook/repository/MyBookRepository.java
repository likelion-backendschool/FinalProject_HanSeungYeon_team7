package com.example.mutbooks.app.mybook.repository;

import com.example.mutbooks.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    Optional<MyBook> findByOwnerIdAndProductId(Long ownerId, Long productId);
}
