package com.example.mutbooks.app.member.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class ModifyForm {
    private String nickname;
    @NotEmpty
    private String email;
}
