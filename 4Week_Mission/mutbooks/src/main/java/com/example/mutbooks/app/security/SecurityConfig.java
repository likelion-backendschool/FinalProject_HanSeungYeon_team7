package com.example.mutbooks.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
//    private final AuthenticationSuccessHandler authenticationSuccessHandler;
//    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login") // GET
                                .loginProcessingUrl("/member/login") // POST
//                                .successHandler(authenticationSuccessHandler)
//                                .failureHandler(authenticationFailureHandler)
                )
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                // spring doc 관리자 회원만 허용
                                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                                .hasAuthority("ADMIN")
                                .anyRequest()
                                .permitAll()
                )
                .logout(
                        logout -> logout.logoutUrl("/member/logout")
                        // 로그아웃 후 기본 이동경로 = /login?logout
                )
                .csrf().disable(); // CSRF 토큰 끄기;

        return http.build();
    }
}
