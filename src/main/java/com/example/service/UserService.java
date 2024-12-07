package com.example.service;

import com.example.command.AuthorizationUserCommand;
import com.example.command.CreateUserCommand;
import com.example.command.UpdateUserCommand;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.InvalidPasswordException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void createUser(CreateUserCommand createUserCommand) {
        String username = createUserCommand.getUsername();
        String email = createUserCommand.getEmail();
        String password = createUserCommand.getPassword();
        User newUser = new User(username, email, password);
        userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserDtoById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserDto getUserByAuthorization(AuthorizationUserCommand authorizationCommand) {
        Optional<User> userOptional = userRepository.findByUsername(authorizationCommand.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(authorizationCommand.getPassword())) {
                return userMapper.toDto(user);
            } else {
                throw new InvalidPasswordException("Invalid password");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UpdateUserCommand updateUserCommand) {
        User user = userRepository.findById(updateUserCommand.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(updateUserCommand.getUsername());
        user.setPassword(updateUserCommand.getPassword());
        user.setEmail(updateUserCommand.getEmail());
        userRepository.save(user);

    }
}
