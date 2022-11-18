package com.example.mutbooks.app.productHashTag.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.productKeyword.entity.ProductKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class ProductHashTag extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;      // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;      // 도서

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductKeyword productKeyword;    // 키워드
}