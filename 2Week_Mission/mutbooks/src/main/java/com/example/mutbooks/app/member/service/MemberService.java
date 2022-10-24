package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.mail.service.MailService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.exception.PasswordNotMatchedException;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PwdModifyForm;
import com.example.mutbooks.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

        return member;
    }

    // 회원기본 정보 수정
    @Transactional
    public void modifyProfile(Member member, ModifyForm modifyForm) {
        // TODO : 작가->일반 회원 될 수 있는지 고민(글 작성자 이름 표시 문제)
        member.modifyInfo(modifyForm.getEmail(), modifyForm.getNickname().trim());
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
    @Transactional
    public Member findByUsernameAndEmail(String username, String email) {
        Member member = memberRepository.findByUsernameAndEmail(username, email).orElse(null);
        // 임시 비번 발급 후, 비밀번호 업데이트
        if(member != null) {
            // 1. 임시 비밀번호 생성(UUID이용)
            String tempPwd= UUID.randomUUID().toString().replace("-", "");//-를 제거
            tempPwd = tempPwd.substring(0,10);  //tempPwd를 앞에서부터 10자리 잘라줌
            // 2. 메일 전송
            mailService.sendTempPassword(username, email, tempPwd);
            // 3. 회원 비밀번호 -> 임시 비밀번호로 변경
            modifyPassword(member, tempPwd);
        }
        return member;
    }

    @Transactional
    public void modifyPassword(Member member, String password) {
        String newPassword = passwordEncoder.encode(password);
        member.modifyPassword(newPassword);
    }

    // 비밀번호 수정
    @Transactional
    public void modifyPassword(Member member, PwdModifyForm pwdModifyForm) {
        // 기존 비밀번호가 맞는지 검증 후 수정
        if(!passwordEncoder.matches(pwdModifyForm.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchedException("기존 비밀번호와 일치하지 않습니다.");
        }

        String newPassword = passwordEncoder.encode(pwdModifyForm.getNewPassword());
        member.modifyPassword(newPassword);
    }
}
