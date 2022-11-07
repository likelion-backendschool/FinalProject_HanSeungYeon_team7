package com.example.mutbooks.app.withdraw.controller;

import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.withdraw.entity.WithdrawApply;
import com.example.mutbooks.app.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/withdraw")
public class AdmWithdrawController {
    private final MemberService memberService;
    private final WithdrawService withdrawService;

    // 출금 신청 내역 조회
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/applyList")
    public String showApplyList(Model model) {
        List<WithdrawApply> withdrawApplies = withdrawService.findAll();
        model.addAttribute("withdrawApplies", withdrawApplies);

        return "adm/withdraw/apply_list";
    }
}
