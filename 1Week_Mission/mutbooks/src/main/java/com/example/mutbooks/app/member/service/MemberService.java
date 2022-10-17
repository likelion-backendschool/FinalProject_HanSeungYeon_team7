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
        mailService.sendMail(member.getUsername(), member.getEmail());

        return member;
    }

    @Transactional
    public void modifyProfile(Long memberId, ModifyForm modifyForm) {
        // TODO : 예외 처리
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException()
        );

        // TODO : 원래 작가 회원인 경우, 닉네임 삭제는 불가하도록 수정(글 작성자 이름 표시 문제때문)
        member.setEmail(modifyForm.getEmail());
        member.setNickname(modifyForm.getNickname());

        memberRepository.save(member);
    }

    // 이메일로 아이디 조회
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
}
