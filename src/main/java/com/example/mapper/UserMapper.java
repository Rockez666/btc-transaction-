package com.example.mapper;

import com.example.dto.UserDto;
import com.example.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final TransactionMapper transactionMapper;
    private final TokenStatisticsMapper tokenStatisticsMapper;

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getTransactions()
                        .stream()
                        .map(transactionMapper::transactionToDto)
                        .collect(Collectors.toList()),
                user.getTokenStatistics()
                        .stream()
                        .map(tokenStatisticsMapper::tokenStatisticsToDto)
                        .collect(Collectors.toList())
        );
    }
}
