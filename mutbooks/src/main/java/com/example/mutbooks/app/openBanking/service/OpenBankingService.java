package com.example.mutbooks.app.openBanking.service;

import com.example.mutbooks.app.openBanking.dto.OpenBankingAccessTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class OpenBankingService {
    private final RestTemplate restTemplate;
//    private final OpenBankingRepository openBankingRepository;

    @Value("${openapi.openBanking.client_id}")
    String clientId;

    @Value("${openapi.openBanking.client_secret}")
    String clientSecret;

    @Transactional
    public OpenBankingAccessTokenDto genAccessToken() {
        // 없으면 새로 발급
        String url = "https://testapi.openbanking.or.kr/oauth/2.0/token";
        // header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "oob");
        params.add("grant_type", "client_credentials");
        // header + body 로 request 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // post 요청 보내기(url, request(헤더 + 파라미터 정보), response 타입)
        OpenBankingAccessTokenDto accessTokenDto = restTemplate.postForObject(
                url,
                request,
                OpenBankingAccessTokenDto.class);
        return accessTokenDto;
    }
}
