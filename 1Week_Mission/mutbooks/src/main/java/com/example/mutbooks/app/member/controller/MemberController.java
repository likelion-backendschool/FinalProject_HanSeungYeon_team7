package com.example.mutbooks.app.member.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // 회원가입 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    // 회원가입
    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {

        }

        memberService.join(joinForm);

        return "redirect:/";
    }

    // 로그인 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    // 회원정보 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/modify";
    }

    // 회원정보 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/profile";
    }

    // 회원정보 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyProfile(@AuthenticationPrincipal MemberContext memberContext, @Valid ModifyForm modifyForm) {
        Long memberId = memberContext.getId();
        memberService.modifyProfile(memberId, modifyForm);

        // TODO: 회원 정보 조회 페이지로 리다이렉트
        return "redirect:/";
    }

    // 아이디 찾기 폼
    @PreAuthorize("isAnonymous")
    @GetMapping("/findUsername")
    public String showFindUsername() {
        return "member/find_username";
    }

    // 이메일로 아이디 찾기
    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUsername(@Valid String email, Model model) {
        Member member = memberService.findByEmail(email);

        model.addAttribute("member", member);

        return "member/confirm_username";
    }
}
