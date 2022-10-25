package com.example.mutbooks.app.cart.service;

import com.example.mutbooks.app.cart.entity.CartItem;
import com.example.mutbooks.app.cart.repository.CartItemRepository;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addCartItem(Member member, Product product) {
        // 이미 장바구니에 담겼는지 검사
        CartItem oldCartItem = cartItemRepository.findByMemberIdAndProductId(member.getId(), product.getId())
                .orElse(null);

        if(oldCartItem != null) {
            return oldCartItem;
        }

        CartItem cartItem = CartItem.builder()
                .member(member)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }
}
