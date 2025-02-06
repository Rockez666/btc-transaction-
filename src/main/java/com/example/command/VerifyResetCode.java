package com.example.command;

import lombok.Data;

@Data
public class VerifyResetCode {
    private String email;
    private String code;

    public VerifyResetCode(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
