package com.example.mutbooks.app.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendJoinCongrats(String username, String email) {
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

    // 임시 비밀번호 발급 메일 전송
    public void sendTempPassword(String username, String email) {
        //임시 비밀번호 생성(UUID이용)
        String tempPw= UUID.randomUUID().toString().replace("-", "");//-를 제거
        tempPw = tempPw.substring(0,10);//tempPw를 앞에서부터 10자리 잘라줌

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
        String title = "[MUTBooks] 회원님의 임시 비밀번호입니다.";
        simpleMessage.setSubject(title);

        // 메일 내용
        String content = """
        %S 님의 임시 비밀번호는 %s 입니다.
        """.formatted(username, tempPw);
        simpleMessage.setText(content);

        // 메일 발송
        javaMailSender.send(simpleMessage);
    }
}
