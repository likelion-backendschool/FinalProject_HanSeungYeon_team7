package com.example.mutbooks.app.member.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class JoinForm {
    @Size(min = 5, max = 20, message = "id는 5 ~ 20자리로 입력해주세요.")
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;

    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    private String nickname;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
