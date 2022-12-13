package com.example.mutbooks.app.member.exception;

public class UsernameDuplicationException extends RuntimeException {
    private static final String MESSAGE = "중복된 아이디입니다.";

    public UsernameDuplicationException() {
        super(MESSAGE);
    }
}
