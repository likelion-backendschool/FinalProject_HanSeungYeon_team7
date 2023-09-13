package com.example.mutbooks.app.member.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class AccountRegisterForm {
    @NotBlank(message = "은행을 선택해주세요.")
    private String bankName;        // 은행명
    @NotBlank(message = "계좌번호를 입력해주세요.")
    private String bankAccountNo;   // 계좌번호
    @NotBlank(message = "예금주 성함을 입력해주세요.")
    private String accountHolderName;   // 예금주 성함
    @NotBlank(message = "예금주 생년월일(6자리)를 입력해주세요")
    private String accountHolderBirth;  // 예금주 생년월일
}
