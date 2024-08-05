package com.fiap.orders.exception;

public class ResourceNotFoundException extends RuntimeException {

    String message;

    public ResourceNotFoundException() {
        super("Resource not Found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
