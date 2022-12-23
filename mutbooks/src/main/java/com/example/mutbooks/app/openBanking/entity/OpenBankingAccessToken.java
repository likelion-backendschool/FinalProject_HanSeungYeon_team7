package com.example.mutbooks.app.openBanking.entity;

import com.example.mutbooks.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class OpenBankingAccessToken extends BaseEntity {
    @Column(nullable = false, length = 400)   // 디폴트 길이는 255인데 토큰은 최대 400자이므로 꼭 설정해야함
    private String accessToken;     // 오픈뱅킹에서 발행된 Access Token
    private String tokenType;       // Access Token 유형(고정값: Bearer)
    private String expiresIn;       // Access Token 만료 기간(초)
    private LocalDateTime expireAt; // Access Token 만료일시
    private String scope;           // Access Token 권한 범위(고정값: oob)
    private String clientUseCode;   // 이용기관코드
}
