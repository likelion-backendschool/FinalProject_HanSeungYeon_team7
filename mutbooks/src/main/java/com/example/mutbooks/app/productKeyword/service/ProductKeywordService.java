package com.example.mutbooks.app.productKeyword.service;

import com.example.mutbooks.app.productKeyword.entity.ProductKeyword;
import com.example.mutbooks.app.productKeyword.repository.ProductKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductKeywordService {
    private final ProductKeywordRepository productKeywordRepository;

    // 도서 해시태그 키워드 저장
    @Transactional
    public ProductKeyword save(String content) {
        Optional<ProductKeyword> optionalProductKeyword = productKeywordRepository.findByContent(content);

        // 1. 해당 키워드가(content)가 DB에 있으면 바로 리턴
        if (optionalProductKeyword.isPresent()) {
            return optionalProductKeyword.get();
        }

        // 2. 해당 키워드(content)가 DB에 없으면 저장
        ProductKeyword productKeyword = ProductKeyword.builder()
                .content(content)
                .build();

        productKeywordRepository.save(productKeyword);

        return productKeyword;
    }
}
