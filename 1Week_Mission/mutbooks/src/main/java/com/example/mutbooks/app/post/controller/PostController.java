package com.example.mutbooks.app.post.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    // 글 상세조회
    @GetMapping("/{id}")
    public String showDetail(@PathVariable long id, Model model) {
        Post post = postService.findById(id);

        model.addAttribute("post", post);

        return "post/detail";
    }

    // 글 작성폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "post/write";
    }

    // 글 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid WriteForm writeForm) {
        Member author = memberContext.getMember();

        Post post = postService.write(author, writeForm);

        return "redirect:/post/%d".formatted(post.getId());
    }

    // 글 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();
        Post post = postService.findById(id);

        // TODO : 예외 처리
        if(!postService.canModify(member, post)) {
            throw new RuntimeException();
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    // 글 수정
    // TODO: 작성폼 DTO 재활용 처리 문제
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext, @Valid WriteForm writeForm) {
        Member member = memberContext.getMember();
        Post post = postService.findById(id);

        // TODO : 예외 처리
        if(!postService.canModify(member, post)) {
            throw new RuntimeException();
        }
        postService.modify(post, writeForm);

        return "redirect:/post/%d".formatted(post.getId());
    }
}
