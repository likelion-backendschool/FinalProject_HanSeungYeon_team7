package com.example.mutbooks.app.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(String username, String email) {
        // 수신 대상을 담을 ArrayList 생성
        ArrayList<String> toUserList = new ArrayList<>();

        // 수신 대상 추가
        toUserList.add(email);

        // 수신 대상 개수
        int toUserSize = toUserList.size();

        // SimpleMailMessage (단순 텍스트 구성 메일 메시지 생성할 때 이용)
        SimpleMailMessage simpleMessage = new SimpleMailMessage();

        // 수신자 설정
        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        // 메일 제목
        String title = "[MUTBooks] %s 회원님 환영합니다.".formatted(username);
        simpleMessage.setSubject(title);

        // 메일 내용
        String content = """
        %S 님의 MUTBooks 가입을 축하합니다.
        """.formatted(username);
        simpleMessage.setText(content);

        // 메일 발송
        javaMailSender.send(simpleMessage);
    }
}
