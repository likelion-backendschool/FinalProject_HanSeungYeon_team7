package com.example.mutbooks.app.base.initData;

import com.example.mutbooks.app.cart.service.CartService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.order.entity.Order;
import com.example.mutbooks.app.order.service.OrderService;
import com.example.mutbooks.app.post.form.WriteForm;
import com.example.mutbooks.app.post.service.PostService;
import com.example.mutbooks.app.product.entity.Product;
import com.example.mutbooks.app.product.form.ProductForm;
import com.example.mutbooks.app.product.service.ProductService;

public interface InitDataBefore {
    default void before(
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService
    ) {
        // 1번 회원(일반, 작가)
        Member member1 = memberService.join(new JoinForm("user1", "1234", "딸기", "user1@test.com"));
        // 2번 회원(일반, 작가)
        Member member2 = memberService.join(new JoinForm("user2", "1234", "초코", "user2@test.com"));
        // 3번 회원(일반)
        Member member3 = memberService.join(new JoinForm("user3", "1234", null, "user3@test.com"));
        // 4번 회원(일반, 관리자)
        Member member4 = memberService.join(new JoinForm("admin", "1234", null, "admin@test.com"));

        // 1번 회원 캐시 50,000원 충전
        memberService.addCash(member1, 50_000, "충전");
        // 3번 회원 캐시 100,000원 충전
        memberService.addCash(member3, 100_000, "충전");

        // 1번 회원 글 작성
        postService.write(member1, new WriteForm("해리포터1-1", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>","#해리포터1"));
        postService.write(member1, new WriteForm("해리포터1-2", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터1"));
        postService.write(member1, new WriteForm("해리포터1-3", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터1"));
        postService.write(member1, new WriteForm("해리포터2-1", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터2"));
        postService.write(member1, new WriteForm("해리포터2-2", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터2"));
        postService.write(member1, new WriteForm("해리포터2-3", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터2"));
        postService.write(member1, new WriteForm("해리포터3-1", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터3"));
        postService.write(member1, new WriteForm("해리포터3-2", "판타지 소설입니다.", "<ul><li><p>판타지 소설입니다.</p></li></ul>", "#해리포터3"));

        // 1번 회원이 1번(해리포터1) 글 키워드 선택 -> 1, 2, 3번 글 도서 등록(1번 도서)
        Product product1 = productService.create(member1, new ProductForm("해리포터1", "판타지 소설입니다.", 18_000, 1L, "#판타지 #소설"));
        // 1번 회원이 2번(해리포터2) 글 키워드 선택 -> 4, 5, 6번 글 도서 등록(2번 도서)
        Product product2 = productService.create(member1, new ProductForm("해리포터2", "판타지 소설입니다.", 20_000, 1L, "#판타지 #소설"));
        // 1번 회원이 3번(해리포터3) 글 키워드 선택 -> 7, 8번 글 도서 등록(3번 도서)
        Product product3 = productService.create(member1, new ProductForm("해리포터3", "판타지 소설입니다.", 22_000, 1L, "#판타지 #소설"));

        // 2번 회원 글 작성
        postService.write(member2, new WriteForm("하트모양 크래커1", "로맨스 소설입니다.", "<ul><li><p>로맨스 소설입니다.</p></li></ul>","#하트모양_크래커"));
        postService.write(member2, new WriteForm("하트모양 크래커2", "로맨스 소설입니다.", "<ul><li><p>로맨스 소설입니다.</p></li></ul>", "#하트모양_크래커"));

        // 2번 회원이 4번(하트모양_크래커) 글 키워드 선택 -> 9, 10번 글 도서 등록(4번 도서)
        Product product4 = productService.create(member2, new ProductForm("하트모양 크래커", "로맨스 소설입니다.", 15000, 3L, "#로맨스 #소설"));

        // 1번 회원이 4번 도서 장바구니 품목 추가(1번 CartItem)
        cartService.addCartItem(member1, product4);

        // 3번 회원이 1, 2, 3, 4번 도서 장바구니 품목 추가(2, 3, 4, 5번 CartItem)
        cartService.addCartItem(member3, product1);
        cartService.addCartItem(member3, product2);
        cartService.addCartItem(member3, product3);
        cartService.addCartItem(member3, product4);

        // 1번 회원 1번 주문 생성
        Order order1 = orderService.createOrder(member1, "1");
        // 3번 회원 2, 3, 4번 주문 생성
        Order order2 = orderService.createOrder(member3, "2,3");
        Order order3 = orderService.createOrder(member3, "4");
        Order order4 = orderService.createOrder(member3, "5");

        // 1번 회원 1번 주문 결제
        orderService.payByRestCashOnly(order1);
        // 3번 회원 2번 주문 결제, 3번 주문 결제 후 환불
        orderService.payByRestCashOnly(order2);
        orderService.payByRestCashOnly(order3);
        orderService.refundByRestCashOnly(order3);
    }
}
