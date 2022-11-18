package com.example.mutbooks.app.mybook.exception;

public class MyBookNotFoundException extends RuntimeException {
    public MyBookNotFoundException(String message) {
        super(message);
    }
}
