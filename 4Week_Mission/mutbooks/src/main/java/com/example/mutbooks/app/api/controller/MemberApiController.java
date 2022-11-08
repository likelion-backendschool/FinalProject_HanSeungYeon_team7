package com.example.mutbooks.app.api.controller;

import com.example.mutbooks.app.member.dto.LoginDto;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        // 입력 데이터 유효성 검증
        if(loginDto.isNotValid()) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findByUsername(loginDto.getUsername());
        // 1. 존재하지 않는 회원
        if(member == null) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }
        // 2. 올바르지 않은 비밀번호
        // matches(비밀번호 원문, 암호화된 비밀번호)
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        // 헤더(Authentication) 에 JWT 토큰 & 바디에 username, password
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", "JWT Token");

        String body = "username : %s, password : %s".formatted(loginDto.getUsername(), loginDto.getPassword());

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
