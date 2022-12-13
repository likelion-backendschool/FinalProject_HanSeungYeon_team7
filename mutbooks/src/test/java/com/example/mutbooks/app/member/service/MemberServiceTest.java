package com.example.mutbooks.app.member.service;

import com.example.mutbooks.app.member.entity.AuthLevel;
import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.exception.EmailDuplicationException;
import com.example.mutbooks.app.member.exception.UsernameDuplicationException;
import com.example.mutbooks.app.member.form.JoinForm;
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
}