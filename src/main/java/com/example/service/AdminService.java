package com.example.service;

import com.example.dto.UserDto;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
