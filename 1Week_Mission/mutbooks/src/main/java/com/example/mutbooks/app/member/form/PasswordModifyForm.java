package com.example.mutbooks.app.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordModifyForm {
    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    private String password;            // 현재 비밀번호

    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;         // 새 비밀번호

    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "새 비밀번호 확인을 입력해주세요.")
    private String newPasswordConfirm;  // 새 비밀번호 확인
}
