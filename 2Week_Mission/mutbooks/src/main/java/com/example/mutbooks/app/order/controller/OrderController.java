package com.example.mutbooks.app.order.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;

    // 주문 생성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createOrder(@AuthenticationPrincipal MemberContext memberContext, String ids) {
        Member buyer = memberContext.getMember();

        Order order = orderService.createOrder(buyer, ids);

        // 주문 상세 페이지로 리다이렉트
        return "redirect:/order/%d".formatted(order.getId());
    }

    // 내 주문 리스트 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member buyer = memberContext.getMember();
        List<Order> orders = orderService.findByBuyer(buyer);

        model.addAttribute("orders", orders);

        return "order/list";
    }

    // 주문 상세조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext, Model model) {
        Order order = orderService.findById(id);
        Member member = memberContext.getMember();
        long restCash = memberService.getRestCash(member);

        // TODO: 예외 처리
        // 주문 조회 권한 검사
        if(orderService.canSelect(member, order) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "order/detail";
    }

    // 주문 취소
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext) {
        Order order = orderService.findById(id);
        Member member = memberContext.getMember();

        // 주문 조회 권한 검사
        if(orderService.canCancel(member, order) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        orderService.cancel(order);

        // 주문 내역 페이지로 리다이렉트
        return "redirect:/order/list";
    }
}
