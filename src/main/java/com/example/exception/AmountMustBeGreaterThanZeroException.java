package com.example.exception;

public class AmountMustBeGreaterThanZeroException extends RuntimeException {
    public AmountMustBeGreaterThanZeroException(String message) {
        super(message);
    }
}
