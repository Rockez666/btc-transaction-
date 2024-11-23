package com.example.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserCommand {
    @NotBlank(message = "userId for update is mandatory")
    private Long userId;
    @NotBlank(message = "username for update is mandatory")
    private String username;
    @Email(message = "Email for update should be valid")
    @NotBlank(message = "email for update is mandatory")
    private String email;
    @NotBlank(message = "password for update is mandatory ")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
    
    public UpdateUserCommand() {}

    public UpdateUserCommand(Long userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
