package com.example.mutbooks.app.product.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.postKeyword.dto.PostKeywordDto;
import com.example.mutbooks.app.postKeyword.service.PostKeywordService;
import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.product.form.ProductForm;
import com.example.mutbooks.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final PostKeywordService postKeywordService;

    // TODO: 작가회원인지 검증
    // 도서 등록폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreate(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        List<PostKeywordDto> postKeywords = postKeywordService.findByMemberId(memberContext.getId());
        model.addAttribute("postKeywords", postKeywords);

        return "/product/create";
    }

    // 도서 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext, @Valid ProductForm productForm) {
        Member author = memberContext.getMember();
        Product product = productService.create(author, productForm);

        return "redirect:/product/" + product.getId();
    }

    // 도서 상세조회
    @GetMapping("/{id}")
    public String showDetail(@PathVariable long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);

        return "product/detail";
    }
}
