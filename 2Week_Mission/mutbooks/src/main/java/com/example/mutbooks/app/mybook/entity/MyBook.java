package com.example.mutbooks.app.mybook.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
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
@Table(indexes = @Index(name="idx__owner_id__product_id", columnList = "owner_id, product_id"))
public class MyBook extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;           // 소유주

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;        // 상품
}
