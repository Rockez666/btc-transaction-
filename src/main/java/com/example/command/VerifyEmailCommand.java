package com.example.command;

import lombok.Data;

@Data
public class VerifyEmailCommand {
    private String email;
    private String verificationCode;

    public VerifyEmailCommand(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public VerifyEmailCommand() {}
}
