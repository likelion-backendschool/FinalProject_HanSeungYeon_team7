package com.example.mutbooks.app.withdraw.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.withdraw.entity.WithdrawApply;
import com.example.mutbooks.app.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/withdraw")
public class AdmWithdrawController {
    private final WithdrawService withdrawService;

    // 출금 신청 내역 조회
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/applyList")
    public String showApplyList(Model model) {
        List<WithdrawApply> withdrawApplies = withdrawService.findAll();
        model.addAttribute("withdrawApplies", withdrawApplies);

        return "adm/withdraw/apply_list";
    }

    // 출금 신청 내역 조회
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{withdrawApplyId}")
    public String withdraw(@PathVariable long withdrawApplyId) {
        withdrawService.withdraw(withdrawApplyId);

        return "redirect:/adm/withdraw/applyList";
    }

    // 출금 취소(관리자)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cancel/{withdrawApplyId}")
    public String cancel(
            @PathVariable long withdrawApplyId,
            @AuthenticationPrincipal MemberContext memberContext
    ) {
        withdrawService.cancelByAdmin(memberContext.getUsername(), withdrawApplyId);
        // 출금 신청 내역 페이지로 리다이렉트
        return "redirect:/withdraw/applyList";
    }
}
