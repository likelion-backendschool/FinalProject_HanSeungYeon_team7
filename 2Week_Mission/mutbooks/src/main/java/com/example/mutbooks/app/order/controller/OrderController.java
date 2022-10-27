package com.example.mutbooks.app.order.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.exception.OrderIdNotMatchedException;
import com.example.mutbooks.app.order.exception.PaymentFailByInsufficientCashException;
import com.example.mutbooks.app.order.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;    // Ut

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

    // 예치금 전액 결제
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/pay")
    public String payByRestCashOnly(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id){
        Order order = orderService.findById(id);
        Member member = memberContext.getMember();
        long restCash = memberService.getRestCash(member);

        if(orderService.canPayment(member, order) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        orderService.payByRestCashOnly(order);

        return "redirect:/order/%d".formatted(order.getId());
    }

    // Toss Payments 시작
    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    private final String SECRET_KEY = "test_sk_jkYG57Eba3GlOkbXE5lVpWDOxmA1";

    // 결제 성공 리다이렉트 URL
    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable long id,              // orderId
            @RequestParam String paymentKey,    // 결제 건에 대한 고유한 키 값
            @RequestParam String orderId,       // 상점에서 주문 건 구분을 위해 발급한 고유ID
            @RequestParam Integer amount,       // 실 결제 금액
            Model model,
            @AuthenticationPrincipal MemberContext memberContext
    ) throws Exception {
        // TODO: id 와 orderId 무결성 검증하는 이유
        Order order = orderService.findById(id);
        long realOrderId = Long.parseLong(orderId.split("__")[1]);

        if(id != realOrderId) {
            throw new OrderIdNotMatchedException("");
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        // 주문 금액 검증 로직 추가
        Member member = memberContext.getMember();
        int restCash = memberService.getRestCash(member);   // 보유 캐시
        // 캐시 결제 금액 = 결제 금액 - pg 결제 금액
        int cashPayPrice = order.getPayPrice() - amount;
        // 캐시 결제 금액 > 보유 캐시 이면, 캐시 부족 예외
        if(cashPayPrice > restCash) {
            throw new PaymentFailByInsufficientCashException("보유 캐시 금액보다 사용 캐시 금액이 더 많습니다.");
        }

        // 1. 결제 승인 API 요청
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        // 2. 응답받은 승인 결과가 성공이면 결제완료 처리
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // 2-1. 결제 완료 처리(캐시, 카드 결제 CashLog 기록 남기기)
            orderService.payByTossPayments(order, cashPayPrice);

            // 2-2. 주문 상세조회로 리다이렉트
            return "redirect:/order/%d".formatted(order.getId());
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    // 결제 실패 리다패렉트 URL
    @RequestMapping("/{id}/fail")
    public String failPayment(
            @RequestParam String message,   // 에러 메시지
            @RequestParam String code,      // 에러 코드
            @RequestParam String orderId,   // 상점에서 주문 건 구분을 위해 발급한 고유ID
            Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        model.addAttribute("orderId", orderId);

        return "order/fail";
    }
    // Toss Payments 끝
}
