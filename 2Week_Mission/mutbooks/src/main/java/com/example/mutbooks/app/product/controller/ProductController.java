package com.example.mutbooks.app.product.controller;

import com.example.mutbooks.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // TODO: 작가회원인지 검증
    // 도서 등록폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreate() {
        return "/product/create";
    }
}
