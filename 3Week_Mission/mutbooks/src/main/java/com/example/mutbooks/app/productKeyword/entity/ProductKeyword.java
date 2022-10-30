package com.example.mutbooks.app.productKeyword.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class ProductKeyword extends BaseEntity {
    @Column(unique = true)
    private String content; // 해시태그
}
