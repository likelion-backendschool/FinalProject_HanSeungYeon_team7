package com.example.mutbooks.app.member.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsernameFindDto {
    @NotBlank(message = "email 을(를) 입력해주세요.")
    private String email;
}
