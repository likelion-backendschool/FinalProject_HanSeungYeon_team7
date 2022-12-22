package com.example.mutbooks.app.member.controller;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.exception.EmailDuplicationException;
import com.example.mutbooks.app.member.exception.PasswordNotMatchedException;
import com.example.mutbooks.app.member.exception.UsernameDuplicationException;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PasswordUpdateForm;
import com.example.mutbooks.app.member.form.WithdrawAccountForm;
import com.example.mutbooks.app.member.service.MemberService;
import com.example.mutbooks.app.member.validator.PwdModifyFormValidator;
import com.example.mutbooks.app.security.dto.MemberContext;
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
        try {
            memberService.join(joinForm);
        } catch(UsernameDuplicationException e) {
            bindingResult.rejectValue("username", "duplicated username", "중복된 아이디 입니다.");
            return "member/join";
        } catch(EmailDuplicationException e) {
            bindingResult.rejectValue("email", "duplicated email", "중복된 이메일 입니다.");
            return "member/join";
        }
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
        memberService.modifyProfile(memberContext.getUsername(), modifyForm);

        return "redirect:/member/profile";
    }

    // 비밀번호 수정폼
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String modifyPassword(PasswordUpdateForm passwordUpdateForm) {
        return "member/modify_password";
    }

    // 비밀번호 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyProfile(@AuthenticationPrincipal MemberContext memberContext,
                                @Valid PasswordUpdateForm passwordUpdateForm, BindingResult bindingResult,
                                HttpServletRequest request) {
        // 유효성 검증 추가
        pwdModifyFormValidator.validate(passwordUpdateForm, bindingResult);
        if(bindingResult.hasErrors()) {
            System.out.println("bindingResult = " + bindingResult.getErrorCount());
            return "member/modify_password";
        }
        try {
            memberService.modifyPassword(memberContext.getUsername(), passwordUpdateForm);
        } catch(PasswordNotMatchedException e) {
            bindingResult.rejectValue("password", "not matched password", "기존 비밀번호가 올바르지 않습니다.");
            return "member/modify_password";
        }
        // 강제 로그아웃 처리 후 로그인 페이지로 리다이렉트
//        try {
//            request.logout();
//        } catch (ServletException e) {
//            throw new RuntimeException(e);
//        }
        // 회원프로필 페이지로 리다이렉트
        return "redirect:/member/profile";
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

    // 비밀번호 찾기 폼
    @PreAuthorize("isAnonymous")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/find_password";
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
        memberService.createBankInfo(memberContext.getUsername(), withDrawAccountForm);
        // 출금 게좌 관리 페이지로 리다이렉트
        return "redirect:/member/manageWithdrawAccount";
    }
}
