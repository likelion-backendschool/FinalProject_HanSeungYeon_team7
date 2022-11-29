package com.example.mutbooks.app.emailVerification.controller;

import com.example.mutbooks.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/emailVerification")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final MemberService memberService;

    @GetMapping("/verify")
    public String verify(@RequestParam String email, @RequestParam String authKey) {
        memberService.verifyEmail(email, authKey);

        return "redirect:/member/login";
    }
}
