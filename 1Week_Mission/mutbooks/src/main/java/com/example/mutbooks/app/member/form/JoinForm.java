package com.example.mutbooks.app.member.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String nickname;
    @NotEmpty
    private String email;
}
