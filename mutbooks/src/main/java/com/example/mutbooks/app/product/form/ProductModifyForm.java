package com.example.mutbooks.app.product.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ProductModifyForm {
    @NotEmpty(message = "도서명을 입력해주세요.")
    private String subject;

    @NotEmpty(message = "도서 설명을 입력해주세요.")
    private String content;

    @NotNull(message = "도서 판매 가격을 입력해주세요.")
    private int price;

    private String productKeywords;  // 도서(상품) 해시태그 키워드
}
