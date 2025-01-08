package com.example.command;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterNewUserCommand {
    @NotBlank(message = "username is mandatory")
    private String username;
    @Email(message = "Email should be valid")
    @NotBlank(message = "email is mandatory")
    private String email;
    @NotBlank(message = "password is mandatory ")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    public RegisterNewUserCommand() {
    }
    public RegisterNewUserCommand(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
