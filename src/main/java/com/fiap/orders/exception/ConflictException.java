package com.fiap.orders.exception;

public class ConflictException extends RuntimeException {
    String message;

    public ConflictException(String message) {
        super(message);
    }
}
