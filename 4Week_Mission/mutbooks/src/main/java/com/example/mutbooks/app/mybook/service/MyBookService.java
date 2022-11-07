package com.example.mutbooks.app.mybook.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.mybook.entity.MyBook;
import com.example.mutbooks.app.mybook.repository.MyBookRepository;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;

    // MyBook 추가
    @Transactional
    public void add(Order order) {
        for(OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            Member buyer = order.getBuyer();

            MyBook oldMyBook = myBookRepository.findByProductIdAndOwnerId(product.getId(), buyer.getId())
                    .orElse(null);
            // (ownerId + productId) DB에 없을 때만 저장(중복 저장 막기)
            if(oldMyBook == null) {
                MyBook myBook = MyBook.builder()
                        .owner(buyer)
                        .product(product)
                        .build();

                myBookRepository.save(myBook);
            }
        }
    }

    // MyBook 삭제
    @Transactional
    public void remove(Order order) {
        for(OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            Member buyer = order.getBuyer();

            myBookRepository.deleteByProductIdAndOwnerId(product.getId(), buyer.getId());
        }
    }

    public MyBook findByProductIdAndOwnerId(long productId, long ownerId) {
        return myBookRepository.findByProductIdAndOwnerId(productId, ownerId).orElse(null);
    }
}
