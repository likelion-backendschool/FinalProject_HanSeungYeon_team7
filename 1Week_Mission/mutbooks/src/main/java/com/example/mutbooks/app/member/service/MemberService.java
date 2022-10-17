package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(JoinForm joinForm) {
        // TODO : 중복 가입 예외 처리
        if (memberRepository.findByUsername(joinForm.getUsername()).isPresent()) {
            throw new RuntimeException();
        }

        // 기본 권한 = 일반
        Member member = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .email(joinForm.getEmail())
                .nickname(joinForm.getNickname())
                .authLevel(3)
                .build();

        memberRepository.save(member);

        // TODO : 가입 축하 이메일 전송

        return member;
    }
}
