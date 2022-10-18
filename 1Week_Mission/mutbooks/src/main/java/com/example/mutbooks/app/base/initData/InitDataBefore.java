package com.example.mutbooks.app.base.initData;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.service.PostService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService) {
        // 작가 회원
        Member member1 = memberService.join(new JoinForm("user1", "1234", "작가", "user1@test.com"));
        // 일반 회원
        Member member2 = memberService.join(new JoinForm("user2", "1234", null, "user2@test.com"));

        // 글 작성
        postService.write(member1, new WriteForm("제목1", "안녕하세요1", "<ul><li><p>안녕하세요1</p></li></ul>","#판타지 #소설 #판타지 #SF"));
        postService.write(member1, new WriteForm("제목2", "안녕하세요2", "<ul><li><p>안녕하세요2</p></li></ul>", null));
    }
}
