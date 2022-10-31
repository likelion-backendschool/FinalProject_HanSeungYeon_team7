package com.example.mutbooks.job.makeRebateOrderItem;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class makeRebateOrderItemJobConfig {
    // 매 분 00초마다 실행
    @Scheduled(cron = "0 * * * * *")
    public void printJob() {
        System.out.println(LocalDateTime.now());
    }
}
