package com.example.mutbooks.app.post.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

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

    // 내글 상세조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String showDetail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model) {
        Member member = memberContext.getMember();
        Post post = postService.findById(id);

        // 조회권한 검사
        if(!postService.canSelect(member, post)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("post", post);

        return "post/detail";
    }
    
    // 내글 리스트 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(@RequestParam(defaultValue = "hashTag") String kwType, @RequestParam(defaultValue = "") String kw
            , @AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member author = memberContext.getMember();

        List<Post> posts = postService.search(author, kwType, kw);

        model.addAttribute("posts", posts);

        return "post/list";
    }

    // 글 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();
        Post post = postService.findById(id);

        // 수정권한 검사
        if(!postService.canModify(member, post)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("post", post);

        return "post/modify";
    }

    // 글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext, @Valid WriteForm writeForm) {
        Member member = memberContext.getMember();
        Post post = postService.findById(id);

        // 수정권한 검사
        if(!postService.canModify(member, post)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        postService.modify(post, writeForm);

        return "redirect:/post/%d".formatted(post.getId());
    }

    // TODO: POST 방식으로 바꾸기
    // 글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext) {
        Member member = memberContext.getMember();
        Post post = postService.findById(id);

        // TODO : 예외 처리
        if(!postService.canDelete(member, post)) {
            throw new RuntimeException();
        }
        postService.delete(post);

        // 글 리스트 페이지로 리다이렉트
        return "redirect:/post/list";
    }
}
