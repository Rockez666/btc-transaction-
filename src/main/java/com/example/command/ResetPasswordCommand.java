package com.example.command;

import lombok.Data;

@Data
public class ResetPasswordCommand {
    private String email;
    private String newPassword;
    private String newPasswordConfirm;

    public ResetPasswordCommand(String email,String newPassword, String newPasswordConfirm) {
        this.email = email;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }
}
