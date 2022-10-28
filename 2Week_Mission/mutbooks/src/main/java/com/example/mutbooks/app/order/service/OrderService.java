package com.example.mutbooks.app.order.service;

import com.example.mutbooks.app.cart.entity.CartItem;
import com.example.mutbooks.app.cart.service.CartService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.mybook.service.MyBookService;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.order.exception.OrderNotFoundException;
import com.example.mutbooks.app.order.repository.OrderRepository;
import com.example.mutbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartService cartService;
    private final MemberService memberService;
    private final MyBookService myBookService;
    private final OrderRepository orderRepository;

    // 선택한 장바구니 품목으로부터 주문 생성
    @Transactional
    public Order createOrder(Member buyer, String ids) {
        // 주문 생성해야하는 cartItem id 리스트
        String[] idsArr = ids.split(",");
        // Array -> List
        List<Long> cartItemIds = Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());
        // 1. 주문 생성해야하는 CartItem 조회
        List<CartItem> cartItems = cartService.findByBuyerAndIdInOrderByIdDesc(buyer, cartItemIds);

        // 2. 주문 품목 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems) {
            // 2-1. 상품으로부터 주문 품목 생성
            Product product = cartItem.getProduct();
            orderItems.add(new OrderItem(product)); // Order 정보는 비어있는 상태에서 먼저 생성
            // 2-2. 장바구니 품목 삭제
            cartService.deleteCartItem(buyer, product);
        }

        // 3. 주문 생성
        return create(buyer, orderItems);
    }

    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .buyer(buyer)
                .build();

        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.makeName();
        order.setOrderDone();
        orderRepository.save(order);

        return order;
    }

    public List<Order> findByBuyer(Member buyer) {
        return orderRepository.findByBuyerIdOrderByIdDesc(buyer.getId());
    }

    public Order findById(long id) {
        return orderRepository.findById(id).orElseThrow(() -> {
            throw new OrderNotFoundException("해당 주문은 존재하지 않습니다.");
        });
    }

    // 주문 취소
    @Transactional
    public void cancel(Order order) {
        order.setCancelDone();
    }

    // 1. 캐시 전액 결제
    @Transactional
    public void payByRestCashOnly(Order order) {
        Member buyer = order.getBuyer();       // 구매자
        int payPrice = order.getPayPrice();    // 결제 금액
        int restCash = buyer.getRestCash();    // 예치금 잔액

        // 예치금 잔액 < 결제 금액 이면, 결제 거절
        if(restCash < payPrice) {
            throw new RuntimeException("보유 캐시가 부족합니다.");
        }
        // 예치금 차감 처리
        memberService.addCash(buyer, payPrice * -1, "상품결제__캐시__주문__%d".formatted(order.getId()));
        // 결제 완료 처리
        order.setPaymentDone();
        orderRepository.save(order);
        // 내 도서에 추가
        myBookService.add(order);
    }

    // 2. TossPayments 결제(TossPayments 전액 결제, 캐시 + TossPayments 혼합 결제)
    @Transactional
    public void payByTossPayments(Order order, int cashPayPrice) {
        Member buyer = order.getBuyer();
        int payPrice = order.getPayPrice();
        int pgPayPrice = payPrice - cashPayPrice;

        // 캐시 결제 내역 CashLog 추가
        if(cashPayPrice > 0) {
            memberService.addCash(buyer, cashPayPrice * -1, "상품결제__캐시__주문__%d".formatted(order.getId()));
        }

        // 카드 결제 내역 CashLog 추가
        memberService.addCash(buyer, pgPayPrice, "상품결제충전__토스페이먼츠");
        memberService.addCash(buyer, pgPayPrice * -1, "상품결제__토스페이먼츠__주문__%d".formatted(order.getId()));
        // 결제 완료 처리
        order.setPaymentDone();
        orderRepository.save(order);
        // 내 도서에 추가
        myBookService.add(order);
    }

    // 캐시 전액 환불
    @Transactional
    public void refund(Order order) {
        Member buyer = order.getBuyer();
        int payPrice = order.getPayPrice();         // 총 결제 금액
        int pgPayPrice = order.getPgPayPrice();     // pg 결제 금액
        int cashPayPrice = payPrice - pgPayPrice;   // 캐시 결제 금액

        // 1. 전액 캐시 환불인 경우
        // 2. 캐시 + 카드 환불인 경우
        if(cashPayPrice > 0) {
            memberService.addCash(buyer, cashPayPrice, "상품환불충전__캐시");
        }

        order.setRefundDone();
        orderRepository.save(order);
        // 내 도서에서 삭제
        myBookService.remove(order);
    }

    // 주문 정보 조회 권한 검증
    public boolean canSelect(Member member, Order order) {
        return member.getId().equals(order.getBuyer().getId());
    }

    // 주문 취소 권한 검증
    public boolean canCancel(Member member, Order order) {
        // TODO: 단순히 권한 체크만 할거면 주문 취소 가능 상태 체크는 빼기
        // 주문 완료 상태가 아니면 주문 취소 불가
        if(!order.isCancellable()) return false;
        return canSelect(member, order);
    }

    // 결제 권한 검증
    public boolean canPayment(Member member, Order order) {
        return canSelect(member, order);
    }

    // 환불 권한 검증
    public boolean canRefund(Member member, Order order) {
        return canSelect(member, order);
    }
}
