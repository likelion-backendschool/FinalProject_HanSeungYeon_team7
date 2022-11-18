package com.example.mutbooks.app.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    /**
     * 메일 발송
     * @param to 수신자(email)
     * @param subject 메일 제목
     * @param text 메일 내용
     */
    public void send(String to, String subject, String text) {
        // 단순 텍스트 구성 메일 메시지 생성
        SimpleMailMessage message = new SimpleMailMessage();
        // message.setFrom(""); from 값을 설정하지 않으면 application-base-addi.yml 의 spring.mail.username 값이 설정된다.
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);   // 메일 발송
    }

    public void sendJoinCongrats(String username, String to) {
        String subject = "[MUTBooks] %s 회원님 환영합니다.".formatted(username);
        String text = """
        %S 님의 MUTBooks 가입을 축하합니다.
        """.formatted(username);

        send(to, subject, text);
    }

    // 임시 비밀번호 발급 메일 전송
    public void sendTempPassword(String username, String to, String tempPwd) {
        String subject = "[MUTBooks] 회원님의 임시 비밀번호입니다.";
        String text = """
        %S 님의 임시 비밀번호는 %s 입니다.
        """.formatted(username, tempPwd);

        send(to, subject, text);
    }
}
