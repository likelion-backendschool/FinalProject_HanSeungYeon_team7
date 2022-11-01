package com.example.mutbooks.app.withdraw.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WithdrawApplyForm {
    @NotNull(message = "출금 금액을 입력해주세요.")
    private int price;              // 출금 금액
}
