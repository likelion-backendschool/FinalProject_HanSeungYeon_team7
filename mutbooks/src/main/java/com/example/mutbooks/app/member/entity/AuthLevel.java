package com.example.mutbooks.app.member.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AuthLevel {
    USER(3, "USER"),
    ADMIN(7, "ADMIN");

    private final int code;
    private final String value;

    AuthLevel(int code, String value) {
        this.code = code;
        this.value = value;
    }

    // Enum 에서 code 값으로 해당 Enum 을 찾는 메서드
    public static AuthLevel ofCode(Integer code) {
        return Arrays.stream(AuthLevel.values())
                .filter(v -> v.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("code=[%d]가 존재하지 않습니다.", code)));
    }
}
