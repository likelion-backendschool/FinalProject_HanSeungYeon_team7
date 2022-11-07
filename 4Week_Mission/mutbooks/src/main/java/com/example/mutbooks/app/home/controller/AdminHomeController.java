package com.example.mutbooks.app.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
public class AdminHomeController {
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showIndex() {
        return "redirect:/adm/home/main";
    }

    // 관리자 메인페이지
    @GetMapping("/home/main")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMain() {
        return "adm/home/main";
    }
}
