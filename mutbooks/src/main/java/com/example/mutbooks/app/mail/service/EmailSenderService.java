package com.example.mutbooks.app.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    /**
     * 메일 발송
     * @param to 수신자(email)
     * @param subject 메일 제목
     * @param text 메일 내용
     */
    @Async
    public void send(String to, String subject, String text) {
        // 단순 텍스트 구성 메일 메시지 생성
        SimpleMailMessage message = new SimpleMailMessage();
        // message.setFrom(""); from 값을 설정하지 않으면 application-base-addi.yml 의 spring.mail.username 값이 설정된다.
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);   // 메일 발송
    }
}
