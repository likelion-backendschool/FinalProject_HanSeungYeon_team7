package com.example.mutbooks.app.member.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    @Column(unique = true)
    private String email;
    private Integer authLevel;  // 권한레벨(3 = 일반, 7 = 관리자)

    // 일반 회원 여부
    public boolean isGeneral() {
        return nickname.isEmpty();
    }

    // 작가 회원 여부
    public boolean isWriter() {
        return isGeneral() == false;
    }

    // 관리자 회원 여부
    public boolean isAdmin() {
        return authLevel.equals(7);
    }
}
