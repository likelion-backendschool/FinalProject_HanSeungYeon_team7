package com.example.mutbooks.app.member.exception;

public class EmailDuplicationException extends RuntimeException {
    private static final String MESSAGE = "중복된 이메일입니다.";

    public EmailDuplicationException() {
        super(MESSAGE);
    }
}
