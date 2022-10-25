package com.example.mutbooks.app.order.service;

import com.example.mutbooks.app.cart.entity.CartItem;
import com.example.mutbooks.app.cart.service.CartService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.order.exception.OrderNotFoundException;
import com.example.mutbooks.app.order.repository.OrderItemRepository;
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
    private final OrderItemRepository orderItemRepository;
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

    // 주문 정보 조회 권한 검증
    public boolean canSelect(Member member, Order order) {
        return member.getId().equals(order.getBuyer().getId());
    }
}
