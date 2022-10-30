package com.example.mutbooks.app.rebate.controller;

import com.example.mutbooks.app.rebate.form.RebateDataForm;
import com.example.mutbooks.app.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public String makeData(RebateDataForm rebateDataForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/adm/rebate/makeData";
        }

        int year = rebateDataForm.getYear();
        int month = rebateDataForm.getMonth();
        rebateService.makeData(year, month);

        return "성공";
    }
}
