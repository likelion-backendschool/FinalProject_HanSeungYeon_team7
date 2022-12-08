package com.example.mutbooks.app.emailVerification.controller;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/email_verification")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final MemberService memberService;

//    // 이메일 인증 메일 전송
//    @GetMapping("/send")
//    public String sendEmailVerification(@RequestParam String email) {
//
//    }

    // 회원가입 이메일 인증
    @GetMapping("/verify")
    public String verify(@RequestParam String email, @RequestParam String token) {
        memberService.verifyEmail(email, token);
        // 로그인 페이지로 리다이렉트
        return "redirect:/member/login";
    }

    // 아이디 찾기 이메일 인증
    @GetMapping("/verify/find_username")
    public String verifyForFindUsername(@RequestParam String email, @RequestParam String token, Model model) {
        Member member = memberService.verifyEmail(email, token);
        model.addAttribute("username", member.getUsername());

        // 아이디 확인 페이지로 리다이렉트
        return "member/confirm_username";
    }
}
