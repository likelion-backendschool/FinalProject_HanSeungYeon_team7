package com.example.mutbooks.app.member.controller;

import com.example.mutbooks.app.base.security.dto.MemberContext;
import com.example.mutbooks.app.mail.service.MailService;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.WithdrawAccountForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PwdModifyForm;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.member.validator.PwdModifyFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;
    private final PwdModifyFormValidator pwdModifyFormValidator;

    // 회원가입 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(JoinForm joinForm) {
        return "member/join";
    }

    // 회원가입
    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm, BindingResult bindingResult, HttpServletRequest request) throws ServletException {
        // 아이디 중복 검사
        Member oldMember = memberService.findByUsername(joinForm.getUsername());
        if (oldMember != null) {
            bindingResult.rejectValue("username", "duplicated username", "중복된 아이디 입니다.");
            return "member/join";
        }
        // 이메일 중복 검사
        oldMember = memberService.findByEmail(joinForm.getEmail());
        if(oldMember != null) {
            bindingResult.rejectValue("email", "duplicated email", "중복된 이메일 입니다.");
            return "member/join";
        }

        Member member = memberService.join(joinForm);
        // TODO: 테스트를 위해 잠시 주석 처리
        // 가입 축하 이메일 전송
//        mailService.sendJoinCongrats(member.getUsername(), member.getEmail());

        // 회원가입 완료 후, 자동 로그인 처리
        try {
            request.login(joinForm.getUsername(), joinForm.getPassword());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/";
    }

    // 로그인 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    // 회원정보 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, ModifyForm modifyForm, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/modify";
    }

    // 회원정보 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyProfile(@AuthenticationPrincipal MemberContext memberContext,
                                @Valid ModifyForm modifyForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "member/modify";
        }

        Member member = memberService.findByUsername(memberContext.getUsername());
        memberService.modifyProfile(member, modifyForm);

        return "redirect:/member/profile";
    }

    // 비밀번호 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String modifyPassword(PwdModifyForm pwdModifyForm) {
        return "member/modify_password";
    }

    // 비밀번호 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyProfile(@AuthenticationPrincipal MemberContext memberContext,
                                @Valid PwdModifyForm pwdModifyForm, BindingResult bindingResult,
                                HttpServletRequest request) {
        // 유효성 검증 추가
        pwdModifyFormValidator.validate(pwdModifyForm, bindingResult);

        if(bindingResult.hasErrors()) {
            System.out.println("bindingResult = " + bindingResult.getErrorCount());
            return "member/modify_password";
        }

        Member member = memberService.findByUsername(memberContext.getUsername());
        memberService.modifyPassword(member, pwdModifyForm);

        // 강제 로그아웃 처리 후 로그인 페이지로 리다이렉트
        try {
            request.logout();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/member/login";
    }

    // 회원정보 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberContext.getMember();

        model.addAttribute("member", member);

        return "member/profile";
    }

    // 아이디 찾기 폼
    @PreAuthorize("isAnonymous")
    @GetMapping("/findUsername")
    public String showFindUsername() {
        return "member/find_username";
    }

    // 이메일로 아이디 찾기
    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUsername(@Valid String email, Model model) {
        Member member = memberService.findByEmail(email);

        model.addAttribute("member", member);

        return "member/confirm_username";
    }

    // 비밀번호 찾기 폼
    @PreAuthorize("isAnonymous")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/find_password";
    }

    // 아이디 + 이메일로 임시 비밀번호 발급하기
    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(@Valid String username, String email, Model model) {
        Member member = memberService.findByUsernameAndEmail(username, email);

        model.addAttribute("member", member);

        return "member/confirm_password";
    }

    // 출금 계좌 관리
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/manageWithdrawAccount")
    public String manageWithdrawAccount(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member member = memberService.findByUsername(memberContext.getUsername());
        model.addAttribute("member", member);

        return "member/manage_withdraw_account";
    }

    // 출금 계좌 등록폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/registerWithdrawAccount")
    public String showRegisterWithdrawAccount(WithdrawAccountForm withDrawAccountForm) {
        return "member/register_withdraw_account";
    }

    // 출금 계좌 등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registerWithdrawAccount")
    public String registerWithdrawAccount(
            @AuthenticationPrincipal MemberContext memberContext,
            @Valid WithdrawAccountForm withDrawAccountForm, BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "member/register_withdraw_account";
        }
        Member member = memberService.findByUsername(memberContext.getUsername());
        memberService.createBankInfo(member, withDrawAccountForm);

        // 출금 게좌 관리 페이지로 리다이렉트
        return "redirect:/member/manageWithdrawAccount";
    }
}
