package com.example.mutbooks.app.product.controller;

import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.postKeyword.dto.PostKeywordDto;
import com.example.mutbooks.app.postKeyword.service.PostKeywordService;
import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.product.form.ProductForm;
import com.example.mutbooks.app.product.form.ProductModifyForm;
import com.example.mutbooks.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final PostKeywordService postKeywordService;

    // 도서 등록폼
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @GetMapping("/create")
    public String showCreate(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        List<PostKeywordDto> postKeywords = postKeywordService.findByMemberId(memberContext.getId());
        model.addAttribute("postKeywords", postKeywords);

        return "/product/create";
    }

    // 도서 등록
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal MemberContext memberContext,
                         @Valid ProductForm productForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/product/create";
        }

        Member author = memberContext.getMember();
        Product product = productService.create(author, productForm);

        return "redirect:/product/" + product.getId();
    }

    // 도서 상세조회
    @GetMapping("/{id}")
    public String detail(@PathVariable long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);

        return "product/detail";
    }

    // 도서 리스트 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<Product> products = productService.findAllByOrderByIdDesc();
        model.addAttribute("products", products);

        return "product/list";
    }

    // 도서 수정폼
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext, Model model) {
        Product product = productService.findById(id);
        Member member = memberContext.getMember();

        // 수정 권한 검사
        if(productService.canModify(member, product) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        model.addAttribute("product", product);

        return "/product/modify";
    }

    // 도서 수정
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable long id,
                         @AuthenticationPrincipal MemberContext memberContext,
                         @Valid ProductModifyForm productModifyForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "product/modify";
        }

        Product product = productService.findById(id);
        Member member = memberContext.getMember();

        // 수정 권한 검사
        if(productService.canModify(member, product) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        productService.modify(product, productModifyForm);

        return "redirect:/product/%d".formatted(product.getId());
    }

    // 도서 삭제
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext) {
        Member member = memberContext.getMember();
        Product product = productService.findById(id);

        // 삭제 권한 검사
        if(productService.canDelete(member, product) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        productService.delete(product);

        return "redirect:/product/list";
    }
}
