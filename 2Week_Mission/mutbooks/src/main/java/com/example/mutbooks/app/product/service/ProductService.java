package com.example.mutbooks.app.product.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import com.example.mutbooks.app.postKeyword.service.PostKeywordService;
import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.product.form.ProductForm;
import com.example.mutbooks.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final PostKeywordService postKeywordService;

    @Transactional
    public Product create(Member author, ProductForm productForm) {
        PostKeyword postKeyword = postKeywordService.findById(productForm.getPostKeywordId());

        Product product = Product.builder()
                .author(author)
                .postKeyword(postKeyword)
                .subject(productForm.getSubject())
                .content(productForm.getContent())
                .price(productForm.getPrice())
                .build();

        productRepository.save(product);

        // TODO : 도서 해시태그 저장

        return product;
    }
}
