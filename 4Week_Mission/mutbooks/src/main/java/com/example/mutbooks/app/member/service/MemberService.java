package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.cash.service.CashService;
import com.example.mutbooks.app.mail.service.MailService;
import com.example.mutbooks.app.member.entity.AuthLevel;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.entity.MemberExtra;
import com.example.mutbooks.app.member.exception.PasswordNotMatchedException;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PwdModifyForm;
import com.example.mutbooks.app.member.form.WithdrawAccountForm;
import com.example.mutbooks.app.member.repository.MemberRepository;
import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final CashService cashService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Member join(JoinForm joinForm) {
        AuthLevel authLevel = AuthLevel.USER;      // 디폴트 USER 권한
        // username 이 admin 인 회원을 관리자 회원으로 설정
        if(joinForm.getUsername().equals("admin")) {
            authLevel = AuthLevel.ADMIN;
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
    public void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.getAuthorities());

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
    public CashLog addCash(Member member, int price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        // 예치금 변동 금액 반영
        int newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);
        // TODO: 관리자 회원이 정산 처리를 할 경우, 정산 대상 회원으로 강제 로그인 되는 문제때문에 잠시 주석 처리
        // addCash() 를 사용하는 메서드 내에서 forceAuthentication()을 호출해야하는지 고민해보기
        // 세션값 강제 수정
        //forceAuthentication(member);

        return cashLog;
    }

    // 계좌 등록
    @Transactional
    public void createBankInfo(Member member, WithdrawAccountForm withDrawAccountForm) {
        MemberExtra memberExtra = MemberExtra.builder()
                .member(member)
                .bankName(withDrawAccountForm.getBankName())
                .bankAccountNo(withDrawAccountForm.getBankAccountNo())
                .build();
        member.modifyMemberExtra(memberExtra);
        // TODO: 계좌 정보는 memberContext 값에 담겨있지 않으므로 세션값 강제 수정할 필요X
        //forceAuthentication(member);
    }

    // AccessToken 발급(발급된게 있으면 바로 리턴)
    @Transactional
    public String genAccessToken(Member member) {
        // 1. DB에서 AccessToken 조회
        String accessToken = member.getAccessToken();
        // 2. 만료시, 토큰 새로 발급
        if (StringUtils.hasLength(accessToken) == false) {
            // 지금으로부터 100년간의 유효기간을 가지는 토큰을 생성, DB에 토큰 저장
            Map<String, Object> claims = member.getAccessTokenClaims();
            accessToken = jwtProvider.generateAccessToken(claims, 60L * 60 * 24 * 365 * 100);
            member.setAccessToken(accessToken);
        }

        return accessToken;
    }

    // 해당 토큰이 화이트 리스트에 있는지 검증
    public boolean verifyWithWhiteList(Member member, String token) {
        return member.getAccessToken().equals(token);
    }
}
