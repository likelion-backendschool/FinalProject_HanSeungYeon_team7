package com.example.mutbooks.app.order.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.order.entity.Order;
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
            @RequestParam String paymentKey,    // 결제 건에 대한 고유한 키 값
            @RequestParam String orderId,       // 상점에서 주문 건 구분을 위해 발급한 고유ID
            @RequestParam Long amount,          // 실 결제 금액
            Model model) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        // TODO : 주문 금액 검증 로직

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
            return "order/success";
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
