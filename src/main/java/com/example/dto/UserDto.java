package com.example.dto;

import com.example.entity.TokenStatistics;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String email;
    private List<TransactionDto> transactionDtos;
    private List<TokenStatisticsDto> tokenStatisticsDtos;


    public UserDto() {
    }

    public UserDto(String userName, String email, List<TransactionDto> orders,List<TokenStatisticsDto> tokenStatisticsDtos) {
        this.username = userName;
        this.email = email;
        this.transactionDtos = orders;
        this.tokenStatisticsDtos = tokenStatisticsDtos;

    }
}
