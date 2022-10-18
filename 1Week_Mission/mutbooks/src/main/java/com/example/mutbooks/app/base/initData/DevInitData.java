package com.example.mutbooks.app.base.initData;

import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.post.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevInitData implements InitDataBefore {
    @Bean
    CommandLineRunner initData(MemberService memberService, PostService postService) {
        return args -> {
            before(memberService, postService);
        };
    }
}