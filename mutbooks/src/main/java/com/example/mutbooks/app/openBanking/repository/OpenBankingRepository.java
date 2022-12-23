package com.example.mutbooks.app.openBanking.repository;

import com.example.mutbooks.app.openBanking.entity.OpenBankingAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OpenBankingRepository extends JpaRepository<OpenBankingAccessToken, Long> {
    // 만료기한이 현재일시 이후인 accessToken 1개 조회
    Optional<OpenBankingAccessToken> findFirstByExpireAtAfter(LocalDateTime dateTime);
}