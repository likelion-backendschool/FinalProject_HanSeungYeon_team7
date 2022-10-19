package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.mail.service.MailService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
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
    private final MailService mailService;

    @Transactional
    public Member join(JoinForm joinForm) {
        // 기본 권한 = 일반
        Member member = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .email(joinForm.getEmail())
                .nickname(joinForm.getNickname())
                .authLevel(3)
                .build();

        memberRepository.save(member);

        // TODO: 테스트를 위해 잠시 주석 처리
        // 가입 축하 이메일 전송
//        mailService.sendJoinCongrats(member.getUsername(), member.getEmail());

        return member;
    }

    // 회원기본 정보 수정
    @Transactional
    public void modifyProfile(Member member, ModifyForm modifyForm) {
        // TODO : 작가->일반 회원 될 수 있는지 고민(글 작성자 이름 표시 문제)
        member.setEmail(modifyForm.getEmail());
        member.setNickname(modifyForm.getNickname());

        memberRepository.save(member);
    }

    // 이메일로 아이디 조회
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    // 아이디로 회원조회
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    // 아이디 + 이메일 회원 조회
    public Member findByUsernameAndEmail(String username, String email) {
        Member member = memberRepository.findByUsernameAndEmail(username, email).orElse(null);
        // 임시 비번 발급
        if(member != null) {
            mailService.sendTempPassword(username, email);
        }
        return member;
    }
}
