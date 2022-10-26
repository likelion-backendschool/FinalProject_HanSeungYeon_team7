package com.example.mutbooks.app.product.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import com.example.mutbooks.app.productHashTag.entity.ProductHashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Product extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;  // 작가

    @ManyToOne(fetch = FetchType.LAZY)
    private PostKeyword postKeyword;    // 게시글 키워드

    private String subject;     // (노출용)상품명

    @Column(columnDefinition = "TEXT")
    private String content;     // 상품설명

    private int price;          // 판매가

    @OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
    private List<ProductHashTag> productHashTags = new ArrayList<>();   // 도서 해시태그 리스트

    // 실제 판매가
    public int getSalePrice() {
        return getPrice();
    }

    // 도매가
    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.7);
    }

    // 해당 도서의 해시태그들을 한 문장으로 반환
    public String getHashTagString() {
        if(productHashTags.isEmpty()) {
            return "";
        }

        return "#" + productHashTags
                .stream()
                .map(hashTag -> hashTag.getProductKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" #"))
                .trim();
    }
}