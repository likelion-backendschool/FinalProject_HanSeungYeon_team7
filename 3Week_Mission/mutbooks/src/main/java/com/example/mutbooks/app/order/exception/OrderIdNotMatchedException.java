package com.example.mutbooks.app.order.exception;

public class OrderIdNotMatchedException extends RuntimeException {
    public OrderIdNotMatchedException(String message) {
        super(message);
    }
}
