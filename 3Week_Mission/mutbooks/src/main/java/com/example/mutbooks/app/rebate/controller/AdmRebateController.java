package com.example.mutbooks.app.rebate.controller;

import com.example.mutbooks.app.rebate.entity.RebateOrderItem;
import com.example.mutbooks.app.rebate.form.RebateDataForm;
import com.example.mutbooks.app.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
public class AdmRebateController {
    private final RebateService rebateService;

    // 정산 데이터 생성폼
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @GetMapping("/makeData")
    public String showMakeData() {
        return "/adm/rebate/makeData";
    }

    // 정산 데이터 생성
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @PostMapping("/makeData")
    public String makeData(RebateDataForm rebateDataForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/adm/rebate/makeData";
        }

        int year = rebateDataForm.getYear();
        int month = rebateDataForm.getMonth();
        rebateService.makeData(year, month);

        // 정산 데이터 리스트 조회 페이지로 리다이렉트
        return "redirect:/adm/rebate/rebateOrderItemList?year=%d&month=%d".formatted(year, month);
    }

    // 정산 데이터 리스트 조회
    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @GetMapping("/rebateOrderItemList")
    public String showRebateOrderItemList(@RequestParam int year, @RequestParam int month, Model model) {
        List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(year, month);
        model.addAttribute("items", items);

        return "/adm/rebate/rebateOrderItemList";
    }
}
