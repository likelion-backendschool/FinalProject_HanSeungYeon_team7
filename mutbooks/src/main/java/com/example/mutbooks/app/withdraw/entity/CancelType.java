package com.example.mutbooks.app.withdraw.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CancelType {
    USER_REQUEST(1, "사용자 요청"),
    ADMIN_REQUEST(2, "관리자 요청");

    private final int code;
    private final String value;

    CancelType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    // Enum 에서 code 값으로 해당 Enum 을 찾는 메서드
    public static CancelType ofCode(String value) {
        if(value == null) return null;
        return Arrays.stream(CancelType.values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("value=[%s]가 존재하지 않습니다.", value)));
    }
}
