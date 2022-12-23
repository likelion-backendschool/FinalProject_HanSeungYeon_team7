package com.example.mutbooks.app.openBanking.controller;

import com.example.mutbooks.app.openBanking.dto.OpenBankingAccessTokenDto;
import com.example.mutbooks.app.openBanking.service.OpenBankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/open-banking")
public class OpenBankingController {
    private final OpenBankingService openBankingService;

    // 오픈뱅킹 AccessToken 발급
    @PostMapping("/token")
    public ResponseEntity<OpenBankingAccessTokenDto> genAccessToken() {
        OpenBankingAccessTokenDto accessTokenDto = openBankingService.genAccessToken();
        return new ResponseEntity<>(accessTokenDto, HttpStatus.OK);
    }
}
