package com.example.mutbooks.app.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * JwtConfig
 * - JWT 비밀키 관리
 */
@Configuration
public class JwtConfig {
    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;      // 비밀키 원문

    // JWT 비밀키 싱글톤 빈 관리
    @Bean
    public SecretKey jwtSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
}
