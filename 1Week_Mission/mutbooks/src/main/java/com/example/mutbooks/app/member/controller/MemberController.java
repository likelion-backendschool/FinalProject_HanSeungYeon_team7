package com.example.mutbooks.app.member.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // 회원가입 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(JoinForm joinForm) {
        return "member/join";
    }

    // 회원가입
    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm, BindingResult bindingResult, HttpServletRequest request) throws ServletException {
        // 아이디 중복 검사
        Member oldMember = memberService.findByUsername(joinForm.getUsername());
        if (oldMember != null) {
            bindingResult.rejectValue("username", "duplicated username", "중복된 아이디 입니다.");
            return "member/join";
        }
        // 이메일 중복 검사
        oldMember = memberService.findByEmail(joinForm.getEmail());
        if(oldMember != null) {
            bindingResult.rejectValue("email", "duplicated email", "중복된 이메일 입니다.");
            return "member/join";
        }

        Member member = memberService.join(joinForm);
        // 회원가입 완료 후, 자동 로그인 처리
        try {
            request.login(joinForm.getUsername(), joinForm.getPassword());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

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
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, ModifyForm modifyForm, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/modify";
    }

    // 회원정보 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyProfile(@AuthenticationPrincipal MemberContext memberContext,
                                @Valid ModifyForm modifyForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "member/modify";
        }

        Member member = memberService.findByUsername(memberContext.getUsername());
        memberService.modifyProfile(member, modifyForm);
        // 세션에 담긴 회원 기본정보 수정
        memberContext.setUpdateDate(member.getUpdateDate());
        memberContext.setNickname(member.getNickname());
        memberContext.setEmail(member.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberContext, member.getPassword(), memberContext.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    // 회원정보 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/profile";
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

    // 비밀번호 찾기 폼
    @PreAuthorize("isAnonymous")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/find_password";
    }

    // 아이디 + 이메일로 임시 비밀번호 발급하기
    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(@Valid String username, String email, Model model) {
        Member member = memberService.findByUsernameAndEmail(username, email);

        model.addAttribute("member", member);

        return "member/confirm_password";
    }
}
