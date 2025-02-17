package com.example.dto;

import com.example.entity.TokenStatistics;
import com.example.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String userId;
    private String username;
    private Role role;
    private List<TransactionDto> transactions;
    private List<TokenStatisticsDto> tokens;


    public UserDto() {
    }

    public UserDto(String userId,String username,Role role,List<TransactionDto> transactions,List<TokenStatisticsDto> tokens) {
       this.userId = userId;
        this.username = username;
        this.role = role;
        this.transactions = transactions;
        this.tokens = tokens;

    }
}
