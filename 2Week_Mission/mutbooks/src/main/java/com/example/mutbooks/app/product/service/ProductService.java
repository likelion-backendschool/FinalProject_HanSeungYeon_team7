package com.example.mutbooks.app.product.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import com.example.mutbooks.app.postKeyword.service.PostKeywordService;
import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.product.exception.ProductNotFoundException;
import com.example.mutbooks.app.product.form.ProductForm;
import com.example.mutbooks.app.product.form.ProductModifyForm;
import com.example.mutbooks.app.product.repository.ProductRepository;
import com.example.mutbooks.app.productHashTag.service.ProductHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final PostKeywordService postKeywordService;
    private final ProductHashTagService productHashTagService;

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

        // 도서 해시태그 적용
        String productKeywords = productForm.getProductKeywords();
        if(productKeywords != null) {
            productHashTagService.apply(product, productKeywords);
        }

        return product;
    }

    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(() -> {
            throw new ProductNotFoundException("해당 도서는 존재하지 않습니다.");
        });
    }

    public List<Product> findAllByOrderByIdDesc() {
        return productRepository.findALlByOrderByIdDesc();
    }

    // 도서 이름, 가격, 설명, 해시태그 수정
    @Transactional
    public void modify(Product product, ProductModifyForm productModifyForm) {
        product.setSubject(productModifyForm.getSubject());
        product.setContent(productModifyForm.getContent());
        product.setPrice(productModifyForm.getPrice());
        // 해시태그 적용
        String productKeywords = productModifyForm.getProductKeywords();
        if(productKeywords != null) {
            productHashTagService.apply(product, productKeywords);
        }
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

    // 수정 권한 검사
    public boolean canModify(Member member, Product product) {
        return member.getId().equals(product.getAuthor().getId());
    }

    // 삭제 권한 검사
    public boolean canDelete(Member member, Product product) {
        return canModify(member, product);
    }
}
