package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.member.entity.AuthLevel;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.exception.EmailDuplicationException;
import com.example.mutbooks.app.member.exception.PasswordNotMatchedException;
import com.example.mutbooks.app.member.exception.UsernameDuplicationException;
import com.example.mutbooks.app.member.form.JoinForm;
import com.example.mutbooks.app.member.form.ModifyForm;
import com.example.mutbooks.app.member.form.PasswordUpdateForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("일반 유저 회원가입")
    void join1() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .username("user5")
                .password("1234")
                .email("user5@test.com")
                .nickname("사과")
                .build();
        // when
        Member member = memberService.join(joinForm);
        // then
        assertThat(member.getUsername()).isEqualTo("user5");
        assertTrue(passwordEncoder.matches("1234", member.getPassword()));
        assertThat(member.getNickname()).isEqualTo("사과");
        assertThat(member.getEmail()).isEqualTo("user5@test.com");
        assertThat(member.getEmailVerified()).isEqualTo(false);
        assertThat(member.getToken()).isNotBlank();
        assertThat(member.getRestCash()).isEqualTo(0);
        assertThat(member.getAuthLevel()).isEqualTo(AuthLevel.USER);
    }

    @Test
    @DisplayName("아이디가 admin 인 회원은 관리자 권한으로 회원가입")
    void join2() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .username("admin")
                .password("1234")
                .email("admin@test.com")
                .nickname("사과")
                .build();
        // when
        Member member = memberService.join(joinForm);
        // then
        assertThat(member.getUsername()).isEqualTo("admin");
        assertTrue(passwordEncoder.matches("1234", member.getPassword()));
        assertThat(member.getNickname()).isEqualTo("사과");
        assertThat(member.getEmail()).isEqualTo("admin@test.com");
        assertThat(member.getEmailVerified()).isEqualTo(false);
        assertThat(member.getToken()).isNotBlank();
        assertThat(member.getRestCash()).isEqualTo(0);
        assertThat(member.getAuthLevel()).isEqualTo(AuthLevel.ADMIN);
    }

    @Test
    @DisplayName("중복된 username 으로 회원가입")
    void join3() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .username("user1")
                .password("1234")
                .email("user5@test.com")
                .nickname("사과")
                .build();
        // when, then
        assertThrows(UsernameDuplicationException.class, () -> memberService.join(joinForm));
    }

    @Test
    @DisplayName("중복된 email 로 회원가입")
    void join4() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .username("user5")
                .password("1234")
                .email("user2@test.com")
                .nickname("사과")
                .build();
        // when, then
        assertThrows(EmailDuplicationException.class, () -> memberService.join(joinForm));
    }

    @Test
    @DisplayName("회원기본정보 수정")
    void modifyProfile() {
        // given
        ModifyForm modifyForm = ModifyForm.builder()
                .email("new@test.com")
                .nickname("new")
                .build();
        // when
        memberService.modifyProfile("user1", modifyForm);
        Member member = memberService.findByUsername("user1");
        // then
        assertThat(member.getEmail()).isEqualTo("new@test.com");
        assertThat(member.getNickname()).isEqualTo("new");
    }

    @Test
    @DisplayName("회원 비밀번호 수정")
    void modifyPassword1() {
        // given
        PasswordUpdateForm passwordUpdateForm = PasswordUpdateForm.builder()
                .password("1234")
                .newPassword("12345")
                .newPasswordConfirm("12345")
                .build();
        // when
        memberService.modifyPassword("user1", passwordUpdateForm);
        Member member = memberService.findByUsername("user1");
        // then
        assertTrue(passwordEncoder.matches("12345", member.getPassword()));
    }

    @Test
    @DisplayName("기존 비밀번호가 일치하지 않으면 비밀번호 수정 불가")
    void modifyPassword2() {
        // given
        PasswordUpdateForm passwordUpdateForm = PasswordUpdateForm.builder()
                .password("123")
                .newPassword("12345")
                .newPasswordConfirm("12345")
                .build();
        // when, then
        assertThrows(PasswordNotMatchedException.class,
                () -> memberService.modifyPassword("user1", passwordUpdateForm));
    }
}