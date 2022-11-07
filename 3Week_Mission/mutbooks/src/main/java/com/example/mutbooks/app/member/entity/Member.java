package com.example.mutbooks.app.member.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

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

    @Convert(converter = AuthLevelConverter.class)
    private AuthLevel authLevel;    // 권한레벨(3 = 일반, 7 = 관리자)

    private int restCash;      // 예치금

    private String bankName;        // 출금 은행명
    private String bankAccountNo;   // 출금 계좌번호

    // 비밀번호 수정
    public void modifyPassword(String newPassword) {
        this.password = newPassword;
    }

    // 기본정보 수정
    public void modifyInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    // 은행정보 수정
    public void modifyBankAccount(String bankName, String bankAccountNo) {
        this.bankName = bankName;
        this.bankAccountNo = bankAccountNo;
    }

    // 권한 부여
    public List<GrantedAuthority> genAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 모든 로그인한 회원에게는 USER 권한 부여
        authorities.add(new SimpleGrantedAuthority(AuthLevel.USER.getValue()));      // 일반 회원

        // nickname 이 있으면 AUTHOR 권한 부여
        if (StringUtils.hasText(nickname)) {
            authorities.add(new SimpleGrantedAuthority("AUTHOR"));  // 작가 회원
        }

        // authLevel 이 7이면 ADMIN 권한 부여
        if (this.authLevel == AuthLevel.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(AuthLevel.ADMIN.getValue()));  // 관리자 회원
        }

        return authorities;
    }
}
