package com.example.dto;

import com.example.entity.TokenStatistics;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private List<TransactionDto> transactions;
    private List<TokenStatisticsDto> tokens;


    public UserDto() {
    }

    public UserDto(Long id,String userName, String email,String password,List<TransactionDto> transactions,List<TokenStatisticsDto> tokens) {
        this.id = id;
        this.username = userName;
        this.email = email;
        this.password = password;
        this.transactions = transactions;
        this.tokens = tokens;

    }
}
