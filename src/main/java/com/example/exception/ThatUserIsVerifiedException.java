package com.example.exception;

public class ThatUserIsVerifiedException extends RuntimeException {
    public ThatUserIsVerifiedException(String message) {
        super(message);
    }
}
