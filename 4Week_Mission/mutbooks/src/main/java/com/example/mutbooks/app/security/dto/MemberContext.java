package com.example.mutbooks.app.security.dto;

import com.example.mutbooks.app.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberContext extends User {
    private final Long id;
    private final LocalDateTime createDate;
    @Setter
    private LocalDateTime updateDate;
    private final String username;
    @Setter
    private String email;
    @Setter
    private String nickname;

    // 예치금
    @Setter
    private int restCash;

    // 임시 추가 필드
//    @Setter
//    private String bankName;
//    @Setter
//    private String bankAccountNo;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        // 예치금 추가
        this.restCash = member.getRestCash();
        // 임시 추가
//        this.bankName = member.getBankName();
//        this.bankAccountNo = member.getBankAccountNo();
    }

    public Member getMember() {
        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .updateDate(updateDate)
                .username(username)
                .email(email)
                .nickname(nickname)
                // 예치금 추가
                .restCash(restCash)
                // 임시 추가
//                .bankName(bankName)
//                .bankAccountNo(bankAccountNo)
                .build();
    }

    public MemberContext(Member member) {
        super(member.getUsername(), "", member.getAuthorities());

        id = member.getId();
        createDate = member.getCreateDate();
        updateDate = member.getUpdateDate();
        username = member.getUsername();
        email = member.getEmail();
//        authorities = member.getAuthorities().stream().collect(Collectors.toSet());
    }

    public String getName() {
        return getUsername();
    }
}
