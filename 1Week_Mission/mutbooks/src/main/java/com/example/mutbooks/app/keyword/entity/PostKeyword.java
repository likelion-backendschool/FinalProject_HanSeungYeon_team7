package com.example.mutbooks.app.keyword.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PostKeyword extends BaseEntity {
    private String content; // 해시태그
}
