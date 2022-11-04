package com.example.mutbooks.app.base.initData;

import com.example.mutbooks.app.cart.service.CartService;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.order.service.OrderService;
import com.example.mutbooks.app.post.service.PostService;
import com.example.mutbooks.app.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
public class DevInitData implements InitDataBefore {
    // initData 실행 여부(2번 생성되는 것을 막기 위함)
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService
    ) {
        return args -> {
            if(initDataDone) return;
            initDataDone = true;

            log.info("DevInitData 실행");
            before(memberService, postService, productService, cartService, orderService);
        };
    }
}