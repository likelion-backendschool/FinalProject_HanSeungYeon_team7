package com.example.mutbooks.app.product.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import com.example.mutbooks.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
}