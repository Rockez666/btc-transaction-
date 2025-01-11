package com.example.exception;

public class ThisUserAlreadyExists extends RuntimeException {

    public ThisUserAlreadyExists(String message) {
        super(message);
    }
}
