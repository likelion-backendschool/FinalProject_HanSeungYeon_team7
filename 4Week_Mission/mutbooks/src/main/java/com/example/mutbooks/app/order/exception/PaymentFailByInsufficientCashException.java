package com.example.mutbooks.app.order.exception;

public class PaymentFailByInsufficientCashException extends RuntimeException {

    public PaymentFailByInsufficientCashException(String message) {
        super(message);
    }
}
