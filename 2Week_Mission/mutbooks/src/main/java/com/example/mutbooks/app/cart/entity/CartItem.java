package com.example.mutbooks.app.cart.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
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
public class CartItem extends BaseEntity {
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
