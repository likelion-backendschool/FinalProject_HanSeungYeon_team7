package com.example.mutbooks.app.productHashTag.repository;

import com.example.mutbooks.app.productHashTag.entity.ProductHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductHashTagRepository extends JpaRepository<ProductHashTag, Long> {
    List<ProductHashTag> findByProductId(Long productId);

    Optional<ProductHashTag> findByProductIdAndProductKeywordId(Long productId, Long productKeywordId);
}
