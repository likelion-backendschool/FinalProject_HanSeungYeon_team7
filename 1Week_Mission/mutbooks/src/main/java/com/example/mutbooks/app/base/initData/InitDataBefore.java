package com.example.mutbooks.app.base.initData;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.service.MemberService;

public interface InitDataBefore {
    default void before(MemberService memberService) {
        // 일반 회원
        Member member1 = memberService.join(new JoinForm("user1", "1234", "작가1", "user1@test.com"));
        // 작가 회원
        Member member2 = memberService.join(new JoinForm("user2", "1234", "작가2", "user2@test.com"));
    }
}
