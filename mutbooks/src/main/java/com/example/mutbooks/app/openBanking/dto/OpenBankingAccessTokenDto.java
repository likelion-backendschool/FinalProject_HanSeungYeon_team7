package com.example.mutbooks.app.openBanking.dto;

import com.example.mutbooks.app.openBanking.entity.OpenBankingAccessToken;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpenBankingAccessTokenDto {
    private String accessToken;     // 오픈뱅킹에서 발행된 Access Token
    private String tokenType;       // Access Token 유형(고정값: Bearer)
    private String expiresIn;       // Access Token 만료 기간(초)
    private String scope;           // Access Token 권한 범위(고정값: oob)
    private String clientUseCode;   // 이용기관코드

    public static OpenBankingAccessToken toEntity(OpenBankingAccessTokenDto accessTokenDto) {
        LocalDateTime expireAt = LocalDateTime.now()
                .plusSeconds(Integer.parseInt(accessTokenDto.getExpiresIn()));
        return OpenBankingAccessToken.builder()
                .accessToken(accessTokenDto.getAccessToken())
                .tokenType(accessTokenDto.getTokenType())
                .expiresIn(accessTokenDto.getExpiresIn())
                .expireAt(expireAt)
                .scope(accessTokenDto.getScope())
                .clientUseCode(accessTokenDto.getClientUseCode())
                .build();
    }

    public static OpenBankingAccessTokenDto toOpenBankingAccessTokenDto(OpenBankingAccessToken accessToken) {
        return OpenBankingAccessTokenDto.builder()
                .accessToken(accessToken.getAccessToken())
                .tokenType(accessToken.getTokenType())
                .expiresIn(accessToken.getExpiresIn())
                .scope(accessToken.getScope())
                .clientUseCode(accessToken.getClientUseCode())
                .build();
    }
}
