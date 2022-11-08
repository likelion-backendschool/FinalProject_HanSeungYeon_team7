package com.example.mutbooks.app.api.controller;

import com.example.mutbooks.app.member.dto.LoginDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
public class MemberApiController {

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return "username : %s, password : %s".formatted(loginDto.getUsername(), loginDto.getPassword());
    }
}
