package com.example.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthorizationUserCommand {
    @NotBlank(message = "username for mandatory is mandatory")
    private String username;
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    public AuthorizationUserCommand() {
    }

    public AuthorizationUserCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
