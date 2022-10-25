package com.example.mutbooks.app.cart.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.cart.entity.CartItem;
import com.example.mutbooks.app.cart.service.CartService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    // 장바구니 품목 추가
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{productId}")
    public String addCartItem(@PathVariable long productId, @AuthenticationPrincipal MemberContext memberContext) {
        Member member = memberContext.getMember();
        Product product = productService.findById(productId);

        cartService.addCartItem(member, product);

        return "redirect:/cart/list";
    }

    // 장바구니 품목 리스트 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        List<CartItem> cartItems = cartService.findAllByMemberIdOrderByIdDesc(memberContext.getId());

        model.addAttribute("cartItems", cartItems);

        return "cart/list";
    }
}
