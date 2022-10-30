package com.example.mutbooks.app.productHashTag.service;

import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.productHashTag.entity.ProductHashTag;
import com.example.mutbooks.app.productHashTag.repository.ProductHashTagRepository;
import com.example.mutbooks.app.productKeyword.entity.ProductKeyword;
import com.example.mutbooks.app.productKeyword.service.ProductKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductHashTagService {
    private final ProductHashTagRepository productHashTagRepository;
    private final ProductKeywordService productKeywordService;

    // 도서(상품)에 해시태그 반영
    @Transactional
    public void apply(Product product, String productKeywords) {
        // 1. 기존 해시태그 가져오기
        List<ProductHashTag> oldHashTags = findByProductId(product.getId());

        // 2. 새로운 해시태그 키워드 리스트
        List<String> keywordContents = Arrays.stream(productKeywords.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        // 3. 삭제할 해시태그 구하기(기존 해시태그 리스트에서 새로운 해시태그 리스트에 없는 것)
        List<ProductHashTag> deleteHashTags = new ArrayList<>();
        for(ProductHashTag oldHashTag : oldHashTags) {
            // 기존에 등록된 해시태그가 새롭게 등록된 해시태그에 포함되었는지 여부
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getProductKeyword().getContent()));

            if(!contains) {
                deleteHashTags.add(oldHashTag);
            }
        }

        // 4. 3번에서 구한 해시태그 삭제
        deleteHashTags.forEach(hashTag -> {
            productHashTagRepository.delete(hashTag);
        });

        // 5. 나머지 해시태그는 저장
        keywordContents.forEach(keywordContent -> {
            save(product, keywordContent);
        });
    }

    // 도서 해시태그 저장
    public ProductHashTag save(Product product, String keywordContent) {
        // 1. keyword 가져오기
        ProductKeyword productKeyword = productKeywordService.save(keywordContent);

        // 2. (productId + keywordId) 가 DB에 있으면 바로 리턴
        ProductHashTag productHashTag = productHashTagRepository.findByProductIdAndProductKeywordId(product.getId(), productKeyword.getId()).orElse(null);
        if(productHashTag != null) {
            return productHashTag;
        }

        // 3. (productId + keywordId) 로 DB에 없으면 productHashTag 저장
        productHashTag = ProductHashTag.builder()
                .member(product.getAuthor())
                .product(product)
                .productKeyword(productKeyword)
                .build();

        productHashTagRepository.save(productHashTag);

        return productHashTag;
    }

    private List<ProductHashTag> findByProductId(Long productId) {
        return productHashTagRepository.findByProductId(productId);
    }
}