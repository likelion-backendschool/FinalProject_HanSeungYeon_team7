package com.example.mutbooks.app.security;

import com.example.mutbooks.app.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
//    private final ApiAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(authenticationEntryPoint)
//                )
                // jwt 사용 기본 설정
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(STATELESS)
                )
                // cors 허용 설정
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                // 로그인 요청, 아이디 찾기 외 모든 요청은 로그인 필수
                                .antMatchers("/api/*/member/login").permitAll()
                                .antMatchers("/api/*/member/username/find").permitAll()
                                .anyRequest()
                                .authenticated() // 최소자격 : 로그인
                )
                // 필터 설정
                .addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .logout().disable();

        return http.build();
    }

    // cors 허용 정책 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");    // 모든 URL 허용
        corsConfiguration.addAllowedHeader("*");    // 모든 Header 허용
        corsConfiguration.addAllowedMethod("*");    // 모든 HTTP METHOD 허용

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}