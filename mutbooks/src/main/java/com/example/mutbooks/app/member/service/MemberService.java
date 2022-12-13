package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.cash.entity.CashLog;
import com.example.mutbooks.app.cash.service.CashService;
import com.example.mutbooks.app.global.mapper.MemberMapper;
import com.example.mutbooks.app.mail.service.EmailSenderService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.entity.MemberExtra;
import com.example.mutbooks.app.member.exception.EmailDuplicationException;
import com.example.mutbooks.app.member.exception.PasswordNotMatchedException;
import com.example.mutbooks.app.member.exception.UsernameDuplicationException;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PwdModifyForm;
import com.example.mutbooks.app.member.form.WithdrawAccountForm;
import com.example.mutbooks.app.member.repository.MemberRepository;
import com.example.mutbooks.app.security.dto.MemberContext;
import com.example.mutbooks.app.security.jwt.JwtProvider;
import com.example.mutbooks.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final CashService cashService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Member join(JoinForm joinForm) {
        checkUsernameDuplication(joinForm.getUsername());
        checkEmailDuplication(joinForm.getEmail());

        String token = Ut.genEmailToken();// 이메일 인증키 생성

        Member member = MemberMapper.INSTANCE.JoinFormToEntity(joinForm);
        member.updatePassword(passwordEncoder.encode(member.getPassword()));
        member.updateToken(token);
        member.grantAuthLevel();

        memberRepository.save(member);
        // 가입 축하 이메일 전송
        String subject = "[MUTBooks] %s 회원님 환영합니다.".formatted(member.getUsername());
        String text = """
        %s 님의 MUTBooks 가입을 축하합니다.
        아래 '메일 인증' 버튼을 클릭하여 회원가입을 완료해주세요.
        <a href='http://localhost:8010/email_verification/verify?email=%s&token=%s' target='_blank'>메일 인증</a>
        """.formatted(member.getUsername(), member.getEmail(), token);
        // TODO: 테스트를 위해 잠시 주석 처리
//        emailSenderService.send(member.getEmail(), subject, text);
        return member;
    }

    // 아이디 중복 검사
    public void checkUsernameDuplication(String username) {
        boolean isDuplicated = memberRepository.existsByUsername(username);
        if(isDuplicated) {
            throw new UsernameDuplicationException();
        }
    }

    // 이메일 중복 검사
    public void checkEmailDuplication(String email) {
        boolean isDuplicated = memberRepository.existsByEmail(email);
        if(isDuplicated) {
            throw new EmailDuplicationException();
        }
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
            // 1. 임시 비밀번호 생성
            String tempPwd = Ut.genTempPassword();
            String subject = "[MUTBooks] 회원님의 임시 비밀번호입니다.";
            String text = """
            %S 님의 임시 비밀번호는 %s 입니다.
            """.formatted(username, tempPwd);
            // 2. 메일 전송
            emailSenderService.send(email, subject, text);
            // 3. 회원 비밀번호 -> 임시 비밀번호로 변경
            modifyPassword(member, tempPwd);
        }
        return member;
    }

    @Transactional
    public void modifyPassword(Member member, String password) {
        String newPassword = passwordEncoder.encode(password);
        member.updatePassword(newPassword);
    }

    // 비밀번호 수정
    @Transactional
    public void modifyPassword(Member member, PwdModifyForm pwdModifyForm) {
        // 기존 비밀번호가 맞는지 검증 후 수정
        if(!passwordEncoder.matches(pwdModifyForm.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchedException("기존 비밀번호와 일치하지 않습니다.");
        }

        String newPassword = passwordEncoder.encode(pwdModifyForm.getNewPassword());
        member.updatePassword(newPassword);
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
            accessToken = jwtProvider.generateAccessToken(claims);
            member.setAccessToken(accessToken);
        }

        return accessToken;
    }

    // 해당 토큰이 화이트 리스트에 있는지 검증
    public boolean verifyWithWhiteList(Member member, String token) {
        return member.getAccessToken().equals(token);
    }

    // TODO : 예외처리
    // 이메일 인증
    @Transactional
    public Member verifyEmail(String email, String authKey) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));
        if(!authKey.equals(member.getToken())) {
            throw new RuntimeException("유효하지 않은 인증키입니다.");
        }
        // 인증 완료 처리
        member.setEmailVerified(true);

        return member;
    }

    // 아이디 찾기 인증 메일 전송
    @Transactional
    public void sendVerificationEmail(String email) {
        Member member = findByEmail(email);

        String token = Ut.genEmailToken();      // 이메일 인증키 생성
        String subject = "[MUTBooks] 아이디 찾기를 위한 인증 메일입니다.".formatted(member.getUsername());
        String text = """
        안녕하세요. MUT Books 입니다.
        이메일 인증을 위해 아래 '메일 인증' 버튼을 클릭해주세요.
        http://localhost:8010/email_verification/verify/find_username?email=%s&token=%s
        """.formatted(member.getEmail(), token);
        emailSenderService.send(member.getEmail(), subject, text);
        // 인증키 변경
        member.setToken(token);
    }
}
