package com.example.mutbooks.app.withdraw.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WithdrawApplyForm {
    // 최소 출금 신청 금액 = 1000원
    @Min(value = 1000, message = "1회 최소 출금 가능한 금액은 1천원입니다.")
    @NotNull(message = "출금 금액을 입력해주세요.")
    private int price;              // 출금 금액
}
