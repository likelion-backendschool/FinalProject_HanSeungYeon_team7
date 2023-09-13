package com.example.mutbooks.app.openBanking.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpenBankingAccountRealNameDto {
    private String rspCode;     // 응답코드(API)
    private String rspMessage;  // 응답메시지(API)
    private String accountNum;  // 계좌번호
    private String bankCodeStd; // 개설기관.표준코드
}
