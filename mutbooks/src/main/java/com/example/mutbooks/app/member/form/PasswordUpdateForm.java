package com.example.mutbooks.app.member.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class PasswordUpdateForm {
    //TODO: 테스트 편의를 위해 잠시 주석처리
    //@Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    private String password;            // 현재 비밀번호

    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;         // 새 비밀번호

    @Size(min = 8, max = 16, message = "비밀번호는 8 ~ 16자리로 입력해주세요.")
    @NotEmpty(message = "새 비밀번호 확인을 입력해주세요.")
    private String newPasswordConfirm;  // 새 비밀번호 확인
}
