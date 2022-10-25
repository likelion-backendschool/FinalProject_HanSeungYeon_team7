package com.example.mutbooks.app.cart.service;

import com.example.mutbooks.app.cart.entity.CartItem;
import com.example.mutbooks.app.cart.exception.CartItemNotFoundException;
import com.example.mutbooks.app.cart.repository.CartItemRepository;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<CartItem> findAllByMemberIdOrderByIdDesc(Long memberId) {
        return cartItemRepository.findAllByMemberIdOrderByIdDesc(memberId);
    }

    @Transactional
    public void deleteCartItem(Member member, Product product) {
        CartItem cartItem = findByMemberIdAndProductId(member.getId(), product.getId());

        cartItemRepository.delete(cartItem);
    }

    public CartItem findByMemberIdAndProductId(Long memberId, Long productId) {
        return cartItemRepository.findByMemberIdAndProductId(memberId, productId).orElseThrow(
                () -> {
                    throw new CartItemNotFoundException("장바구니 품목이 존재하지 않습니다.");
                });
    }
}
