package com.example.mutbooks.app.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PasswordModifyForm {
    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    private String password;            // 현재 비밀번호
    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;         // 새 비밀번호
    @NotEmpty(message = "새 비밀번호 확인을 입력해주세요.")
    private String newPasswordConfirm;  // 새 비밀번호 확인
}
