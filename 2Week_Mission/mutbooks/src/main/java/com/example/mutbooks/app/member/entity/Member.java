package com.example.mutbooks.app.member.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
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
    private Integer authLevel;  // 권한레벨(3 = 일반, 7 = 관리자)
    private long restCash;      // 예치금

    // 비밀번호 수정
    public void modifyPassword(String newPassword) {
        this.password = newPassword;
    }

    // 기본정보 수정
    public void modifyInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    // 권한 부여
    public List<GrantedAuthority> genAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));      // 일반 회원

        // nickname 이 있으면 작가 권한
        if (StringUtils.hasText(nickname)) {
            authorities.add(new SimpleGrantedAuthority("AUTHOR"));  // 작가 회원
        }

        // authLevel 이 7이면 관리자 권한
        if (this.authLevel == 7) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));  // 관리자 회원
        }

        return authorities;
    }
}
