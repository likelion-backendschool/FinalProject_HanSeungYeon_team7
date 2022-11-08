package com.example.mutbooks.app.api.member.controller;

import com.example.mutbooks.app.api.member.dto.response.MemberDto;
import com.example.mutbooks.app.base.dto.RsData;
import com.example.mutbooks.app.api.member.dto.request.LoginDto;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
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

        log.debug("Ut.json.toStr(member.getAccessTokenClaims()) : " + Ut.json.toStr(member.getAccessTokenClaims()));
        // accessToken 발급
        String accessToken = memberService.genAccessToken(member);
        // 응답 헤더, 바디에 accessToken 담기
        return Ut.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Ut.mapOf("Authentication", accessToken)
                ),
                Ut.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

    // 회원 정보
    @GetMapping("/me")
    public ResponseEntity<RsData> test(@AuthenticationPrincipal MemberContext memberContext) {
        if(memberContext == null) {
            return Ut.spring.responseEntityOf(RsData.failOf(null));
        }
        MemberDto memberDto = MemberDto.toDto(memberContext.getMember());

        return Ut.spring.responseEntityOf(RsData.successOf(Ut.mapOf("member", memberDto)));
    }
}
