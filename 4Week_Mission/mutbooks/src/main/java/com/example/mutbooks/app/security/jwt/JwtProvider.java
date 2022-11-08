package com.example.mutbooks.app.security.jwt;

import com.example.mutbooks.util.Ut;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JwtProvider
 * - JWT 토큰 생성, 검증에 관여
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final SecretKey jwtSecretKey;   // 비밀키

    private SecretKey getSecretKey() {
        return jwtSecretKey;
    }

    // JWT Access Token 발급
    public String generateAccessToken(Map<String, Object> claims, int seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

        return Jwts.builder()
                .claim("body", Ut.json.toStr(claims))         // Claims 정보 설정
                .setExpiration(accessTokenExpiresIn)                // accessToken 만료 시간 설정
                .signWith(getSecretKey(), SignatureAlgorithm.HS512) // HS512, 비밀키로 서명
                .compact();                                         // 토큰 생성
    }

    // JWT Access Token 검증
    public boolean verify(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())  // 비밀키
                    .build()
                    .parseClaimsJws(accessToken);   // 파싱 및 검증(실패시 에러)
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었을 경우
            return false;
        }
        catch (Exception e) {
            // 그 외 에러
            return false;
        }
        return true;
    }

    // accessToken 으로부터 Claim 정보 얻기
    public Map<String, Object> getClaims(String accessToken) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("body", String.class);

        return Ut.json.toMap(body);
    }
}
