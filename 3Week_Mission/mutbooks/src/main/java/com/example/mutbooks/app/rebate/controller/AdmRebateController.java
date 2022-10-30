package com.example.mutbooks.app.rebate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
public class AdmRebateController {
    // 정산 데이터 생성폼
    @GetMapping("/makeData")
    public String showMakeData() {
        return "/adm/rebate/makeData";
    }
}
