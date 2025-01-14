package com.example.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserPasswordCommand {
    @NotBlank(message = "password for update is mandatory ")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
    
    public UpdateUserPasswordCommand() {}

    public UpdateUserPasswordCommand(String password) {
        this.password = password;
    }
}
