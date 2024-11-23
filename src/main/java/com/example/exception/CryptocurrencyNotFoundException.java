package com.example.exception;

public class CryptocurrencyNotFoundException extends RuntimeException {

    public CryptocurrencyNotFoundException(String message) {
        super(message);
    }
}
