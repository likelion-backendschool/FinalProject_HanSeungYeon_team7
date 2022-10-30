package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.cash.service.CashService;
import com.example.mutbooks.app.mail.service.MailService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.exception.PasswordNotMatchedException;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PwdModifyForm;
import com.example.mutbooks.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CashService cashService;

    @Transactional
    public Member join(JoinForm joinForm) {
        int authLevel = 3;      // 디폴트 일반 권한
        // TODO: username 이 admin 인 회원을 관리자 회원으로 설정
        if(joinForm.getUsername().equals("admin")) {
            authLevel = 7;
        }

        // 기본 권한 = 일반
        Member member = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .email(joinForm.getEmail())
                .nickname(joinForm.getNickname())
                .authLevel(authLevel)
                .build();

        memberRepository.save(member);

        return member;
    }

    // 회원기본 정보 수정
    @Transactional
    public void modifyProfile(Member member, ModifyForm modifyForm) {
        // TODO : 작가->일반 회원 될 수 있는지 고민(글 작성자 이름 표시 문제)
        member.modifyInfo(modifyForm.getEmail(), modifyForm.getNickname().trim());

        forceAuthentication(member);
    }

    // 세션에 담긴 회원 기본정보 강제 수정
    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.genAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        member.getPassword(),
                        memberContext.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
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

    // 회원의 남은 예치금 잔액 조회
    public int getRestCash(Member member) {
        return findByUsername(member.getUsername()).getRestCash();
    }

    // 예치금 변동(넣기, 빼기)
    @Transactional
    public void addCash(Member member, int price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        // 예치금 변동 금액 반영
        int newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);
    }
}
