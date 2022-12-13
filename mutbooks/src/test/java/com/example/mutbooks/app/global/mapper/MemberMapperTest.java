package com.example.mutbooks.app.global.mapper;

import com.example.mutbooks.app.member.entity.Member;
import com.example.mutbooks.app.member.form.JoinForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberMapperTest {

    @Test
    @DisplayName("회원가입폼을 엔티티에 매핑하기(emailVerified, restCash 디폴트 값 매핑)")
    void joinFormToEntity() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .username("user5")
                .password("1234")
                .email("user5@test.com")
                .nickname("사과")
                .build();
        // when
        Member member = MemberMapper.INSTANCE.JoinFormToEntity(joinForm);
        // then
        assertThat(member.getUsername()).isEqualTo("user5");
        assertThat(member.getPassword()).isEqualTo("1234");
        assertThat(member.getEmail()).isEqualTo("user5@test.com");
        assertThat(member.getNickname()).isEqualTo("사과");
        assertThat(member.getEmailVerified()).isEqualTo(false);
        assertThat(member.getRestCash()).isEqualTo(0);
    }

    @Test
    @DisplayName("회원가입폼의 닉네임이 null 일 때 엔티티에 매핑하기")
    void joinFormToEntity2() {
        // given
        JoinForm joinForm = JoinForm.builder()
                .username("user5")
                .password("1234")
                .email("user5@test.com")
                .build();
        // when
        Member member = MemberMapper.INSTANCE.JoinFormToEntity(joinForm);
        // then
        assertThat(member.getUsername()).isEqualTo("user5");
        assertThat(member.getPassword()).isEqualTo("1234");
        assertThat(member.getEmail()).isEqualTo("user5@test.com");
        assertThat(member.getNickname()).isNull();
        assertThat(member.getEmailVerified()).isEqualTo(false);
        assertThat(member.getRestCash()).isEqualTo(0);
    }
}