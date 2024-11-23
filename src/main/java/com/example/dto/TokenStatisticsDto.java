package com.example.dto;

import com.example.enums.Cryptocurrency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenStatisticsDto {
    private Cryptocurrency cryptocurrency;
    private BigDecimal totalTokens;
    private BigDecimal averagePurchasePrice;
    private BigDecimal averageSellPrice;

    public TokenStatisticsDto() {
    }

    public TokenStatisticsDto(Cryptocurrency cryptocurrency, BigDecimal totalTokens, BigDecimal averagePurchasePrice,BigDecimal averageSellPrice) {
        this.cryptocurrency = cryptocurrency;
        this.totalTokens = totalTokens;
        this.averagePurchasePrice = averagePurchasePrice;
        this.averageSellPrice = averageSellPrice;
    }
}
