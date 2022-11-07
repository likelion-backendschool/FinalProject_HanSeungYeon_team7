package com.example.mutbooks.app.withdraw.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.withdraw.form.WithdrawApplyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {
    private final MemberService memberService;

    // 출금 신청 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/apply")
    public String showApply(@AuthenticationPrincipal MemberContext memberContext, WithdrawApplyForm withdrawApplyForm, Model model) {
        Member member = memberService.findByUsername(memberContext.getUsername());
        model.addAttribute("member", member);

        return "withdraw/apply";
    }
}
