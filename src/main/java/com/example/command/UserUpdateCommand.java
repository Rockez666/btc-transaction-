package com.example.command;

import com.example.entity.Transaction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserUpdateCommand {
    private String username;
    private String password;
}
