package com.example.mutbooks.app.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class WithdrawAccountForm {
    @NotBlank(message = "은행을 선택해주세요.")
    private String bankName;        // 은행명
    @NotBlank(message = "계좌번호를 입력해주세요.")
    private String bankAccountNo;   // 계좌번호
}
