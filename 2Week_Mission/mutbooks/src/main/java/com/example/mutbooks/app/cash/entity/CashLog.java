package com.example.mutbooks.app.cash.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.example.mutbooks.app.member.entity.Member;
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
public class CashLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int price;         // 변동 금액
    private String eventType;   // 변동 사유
}
