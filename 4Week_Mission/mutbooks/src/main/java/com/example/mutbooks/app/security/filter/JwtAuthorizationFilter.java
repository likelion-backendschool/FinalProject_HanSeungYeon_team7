package com.example.mutbooks.app.security.filter;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 매 요청마다 컨트롤러에 요청이 도달하기 전에 필터를 거쳐 인증/인가 수행
 * 1. 요청 헤더의 Access Token 유효성 검증
 * 2. 토큰으로부터 회원 정보(claims)를 이용해 DB에서 Member 객체 조회
 * 3. 해당 회원 강제 로그인 처리(MemberContext 세션 등록)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String barerToken = request.getHeader("Authorization");
        // 토큰 유효성 검증
        if(barerToken != null) {
            String token = barerToken.substring("Barer ".length());
            // 토큰이 유효하면 회원 정보 얻어서 강제 로그인 처리
            if(jwtProvider.verify(token)) {
                Map<String, Object> claims = jwtProvider.getClaims(token);
                String username = (String) claims.get("username");
                Member member = memberService.findByUsername(username);

                if(member != null) {
                    forceAuthentication(member);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // 강제 로그인 처리
    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        null,
                        member.getAuthorities()
                );

        // 이후 컨트롤러 단에서 MemberContext 객체 사용O
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}