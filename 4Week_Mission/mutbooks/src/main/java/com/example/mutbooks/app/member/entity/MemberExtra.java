package com.example.mutbooks.app.member.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class MemberExtra extends BaseEntity {
    @OneToOne
    private Member member;          // 관련 회원

    private String bankName;        // 출금 은행명
    private String bankAccountNo;   // 출금 계좌번호
}
