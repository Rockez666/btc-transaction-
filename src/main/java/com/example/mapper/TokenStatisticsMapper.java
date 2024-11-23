package com.example.mapper;

import com.example.dto.TokenStatisticsDto;
import com.example.entity.TokenStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenStatisticsMapper {
    public TokenStatisticsDto tokenStatisticsToDto(TokenStatistics tokenStatistics) {
        if (tokenStatistics == null) {
            return null;
        }

        return new TokenStatisticsDto(
                tokenStatistics.getCryptocurrency(),
                tokenStatistics.getTotalTokens(),
                tokenStatistics.getAveragePurchasePrice(),
                tokenStatistics.getAverageSellPrice()
        );

    }
}
