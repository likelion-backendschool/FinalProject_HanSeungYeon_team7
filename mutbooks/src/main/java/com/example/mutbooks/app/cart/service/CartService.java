package com.example.mutbooks.app.cart.service;

import com.example.mutbooks.app.cart.entity.CartItem;
import com.example.mutbooks.app.cart.exception.CartItemNotFoundException;
import com.example.mutbooks.app.cart.repository.CartItemRepository;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addCartItem(Member buyer, Product product) {
        // 이미 장바구니에 담겼는지 검사
        CartItem oldCartItem = cartItemRepository.findByBuyerIdAndProductId(buyer.getId(), product.getId())
                .orElse(null);

        if(oldCartItem != null) {
            return oldCartItem;
        }

        CartItem cartItem = CartItem.builder()
                .buyer(buyer)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    public List<CartItem> findAllByBuyerIdOrderByIdDesc(Long buyerId) {
        return cartItemRepository.findAllByBuyerIdOrderByIdDesc(buyerId);
    }

    @Transactional
    public void deleteCartItem(Member buyer, Product product) {
        CartItem cartItem = findByBuyerIdAndProductId(buyer.getId(), product.getId());

        cartItemRepository.delete(cartItem);
    }

    public CartItem findByBuyerIdAndProductId(Long buyerId, Long productId) {
        return cartItemRepository.findByBuyerIdAndProductId(buyerId, productId).orElseThrow(
                () -> {
                    throw new CartItemNotFoundException("장바구니 품목이 존재하지 않습니다.");
                });
    }

    public CartItem findById(long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    public List<CartItem> findByBuyerAndIdInOrderByIdDesc(Member buyer, List<Long> cartItemIds) {
        return cartItemRepository.findByBuyerIdAndIdInOrderByIdDesc(buyer.getId(), cartItemIds);
    }

    @Transactional
    public void deleteCartItems(Member buyer, String ids) {
        // 장바구니에서 삭제해야하는 cartItem id 리스트
        String[] idsArr = ids.split(",");
        // Array -> List
        List<Long> cartItemIds = Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());

        cartItemRepository.deleteAllByBuyerIdAndIdIn(buyer.getId(), cartItemIds);
    }
}
