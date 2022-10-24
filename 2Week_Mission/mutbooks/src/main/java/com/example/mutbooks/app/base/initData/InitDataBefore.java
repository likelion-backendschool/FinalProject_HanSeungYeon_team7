package com.example.mutbooks.app.base.initData;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.service.PostService;
import com.example.mutbooks.app.product.form.ProductForm;
import com.example.mutbooks.app.product.service.ProductService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService) {
        // 작가 회원
        Member member1 = memberService.join(new JoinForm("user1", "1234", "작가", "user1@test.com"));
        // 일반 회원
        Member member2 = memberService.join(new JoinForm("user2", "1234", null, "user2@test.com"));

        // 글 작성
        postService.write(member1, new WriteForm("제목1", "안녕하세요1", "<ul><li><p>안녕하세요1</p></li></ul>","#판타지 #소설 #판타지 #SF"));
        postService.write(member1, new WriteForm("제목2", "안녕하세요2", "<ul><li><p>안녕하세요2</p></li></ul>", "#판타지 #로맨스"));

        // 1번 회원이 1번(판타지) 글 키워드 선택 -> 1, 2번 글 도서 등록
        productService.create(member1, new ProductForm("해리포터", "판타지 소설입니다.", 10000, 1L, "#판타지 #소설"));
    }
}
