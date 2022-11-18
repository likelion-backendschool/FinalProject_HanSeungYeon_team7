package com.example.mutbooks.app.withdraw.controller;

import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.withdraw.entity.WithdrawApply;
import com.example.mutbooks.app.withdraw.form.WithdrawApplyForm;
import com.example.mutbooks.app.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {
    private final MemberService memberService;
    private final WithdrawService withdrawService;

    // 출금 신청 폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/apply")
    public String showApply(@AuthenticationPrincipal MemberContext memberContext, WithdrawApplyForm withdrawApplyForm, Model model) {
        Member member = memberService.findByUsername(memberContext.getUsername());
        // 출금 계좌 정보가 존재하지 않으면, 출금 계좌 관리 페이지로 리다이렉트
        if(!member.hasBankInfo()) {
            return "redirect:/member/manageWithdrawAccount";
        }
        model.addAttribute("member", member);

        return "withdraw/apply";
    }

    // 출금 신청
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/apply")
    public String apply(
            @AuthenticationPrincipal MemberContext memberContext,
            @Valid WithdrawApplyForm withdrawApplyForm, BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "withdraw/apply";
        }

        withdrawService.apply(memberContext.getUsername(), withdrawApplyForm);
        // 출금 신청 내역 페이지로 리다이렉트
        return "redirect:/withdraw/applyList";
    }

    // 출금 신청 내역 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/applyList")
    public String showApplyList(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        List<WithdrawApply> withdrawApplies = withdrawService.findByApplicantIdOrderByIdDesc(memberContext.getId());
        model.addAttribute("withdrawApplies", withdrawApplies);

        return "withdraw/apply_list";
    }

    // 출금 취소
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel/{withdrawApplyId}")
    public String cancel(
            @PathVariable long withdrawApplyId,
            @AuthenticationPrincipal MemberContext memberContext
    ) {
        withdrawService.cancel(memberContext.getUsername(), withdrawApplyId);
        // 출금 신청 내역 페이지로 리다이렉트
        return "redirect:/withdraw/applyList";
    }
}
