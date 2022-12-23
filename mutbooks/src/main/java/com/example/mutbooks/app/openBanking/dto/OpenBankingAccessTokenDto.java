package com.example.mutbooks.app.openBanking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class OpenBankingAccessTokenDto {
    private String accessToken;     // 오픈뱅킹에서 발행된 Access Token
    private String tokenType;       // Access Token 유형(고정값: Bearer)
    private String expiresIn;       // Access Token 만료 기간(초)
    private String scope;           // Access Token 권한 범위(고정값: oob)
    private String clientUseCode;   // 이용기관코드
}
