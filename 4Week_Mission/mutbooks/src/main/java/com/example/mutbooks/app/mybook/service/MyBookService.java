package com.example.mutbooks.app.mybook.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.mybook.dto.response.MyBookDetailDto;
import com.example.mutbooks.app.mybook.dto.response.MyBookDto;
import com.example.mutbooks.app.mybook.entity.MyBook;
import com.example.mutbooks.app.mybook.exception.MyBookNotFoundException;
import com.example.mutbooks.app.mybook.repository.MyBookRepository;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.entity.OrderItem;
import com.example.mutbooks.app.post.entity.Post;
import com.example.mutbooks.app.postHashTag.entity.PostHashTag;
import com.example.mutbooks.app.postHashTag.service.PostHashTagService;
import com.example.mutbooks.app.postKeyword.entity.PostKeyword;
import com.example.mutbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;
    private final PostHashTagService postHashTagService;

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

    public List<MyBookDto> findAllByOwner(Member owner) {
        List<MyBook> myBooks = myBookRepository.findByOwner(owner);

        List<MyBookDto> myBookDtos = myBooks.stream()
                .map(myBook -> MyBookDto.toDto(myBook))
                .collect(Collectors.toList());

        return myBookDtos;
    }

    public MyBook findById(long id) {
        return myBookRepository.findById(id).orElseThrow(() -> {
            throw new MyBookNotFoundException("");
        });
    }

    public MyBookDetailDto findByIdForDetail(long id) {
        MyBook myBook = findById(id);

        PostKeyword postKeyword = myBook.getProduct().getPostKeyword();
        List<PostHashTag> postHashTags = postHashTagService.findByPostKeyword(postKeyword);

        List<Post> posts = postHashTags.stream()
                .map(postHashTag -> postHashTag.getPost())
                .collect(Collectors.toList());

        // 본인이 소유한 도서인지 검증
        return MyBookDetailDto.toDto(myBook, posts);
    }
}
