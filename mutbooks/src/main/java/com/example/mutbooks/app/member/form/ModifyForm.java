package com.example.mutbooks.app.member.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class ModifyForm {
    private String nickname;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
