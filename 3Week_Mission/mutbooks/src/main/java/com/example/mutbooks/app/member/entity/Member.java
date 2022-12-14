package com.example.mutbooks.app.member.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.*;
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

    // Member 의 memberExtra 에 값이 저장될 때, MemberExtra 도 같이 저장되도록
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberExtra memberExtra;

    // 비밀번호 수정
    public void modifyPassword(String newPassword) {
        this.password = newPassword;
    }

    // 기본정보 수정
    public void modifyInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    // 추가정보 수정
    public void modifyMemberExtra(MemberExtra memberExtra) {
        this.memberExtra = memberExtra;
    }

    // 출금 계좌 정보 등록 여부
    public boolean hasBankInfo() {
        if(memberExtra == null) return false;
        if(memberExtra.getBankName() == null) return false;
        if(memberExtra.getBankAccountNo() == null) return false;
        return true;
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
