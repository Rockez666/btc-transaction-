package com.example.dto;

import com.example.entity.TokenStatistics;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private List<TransactionDto> transactions;
    private List<TokenStatisticsDto> tokens;


    public UserDto() {
    }

    public UserDto(String userName,List<TransactionDto> transactions,List<TokenStatisticsDto> tokens) {
        this.username = userName;
        this.transactions = transactions;
        this.tokens = tokens;

    }
}
