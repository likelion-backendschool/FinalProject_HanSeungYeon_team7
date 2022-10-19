package com.example.mutbooks.app.member.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class JoinForm {
    @Size(min = 5, max = 20, message = "id는 5 ~ 20자리로 입력해주세요.")
    @NotEmpty
    private String username;
    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty
    private String password;
    private String nickname;
    @NotEmpty
    private String email;
}
