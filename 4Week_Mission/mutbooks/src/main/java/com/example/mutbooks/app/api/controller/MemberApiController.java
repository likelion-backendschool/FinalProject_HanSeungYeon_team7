package com.example.mutbooks.app.api.controller;

import com.example.mutbooks.app.base.dto.RsData;
import com.example.mutbooks.app.member.dto.LoginDto;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<RsData> login(@RequestBody LoginDto loginDto) {
        // 입력 데이터 유효성 검증
        if(loginDto.isNotValid()) {
            return Ut.spring.responseEntityOf(RsData.of("F-1", "로그인 정보가 올바르지 않습니다.."));
        }

        Member member = memberService.findByUsername(loginDto.getUsername());
        // 1. 존재하지 않는 회원
        if(member == null) {
            return Ut.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }
        // 2. 올바르지 않은 비밀번호
        // matches(비밀번호 원문, 암호화된 비밀번호)
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            return Ut.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        String accessToken = "JWT Access Token";
        // 응답 헤더, 바디에 accessToken 담기
        return Ut.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, JWT AccessToken 을 발급합니다.",
                        Ut.mapOf("Authentication", accessToken)
                ),
                Ut.spring.httpHeadersOf("Authentication", accessToken)
        );
    }
}
