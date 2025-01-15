package com.example.service;

import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.command.UpdateUserPasswordCommand;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.Role;
import com.example.exception.RoleNotFoundException;
import com.example.exception.ThisUserAlreadyExists;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.security.CustomUserDetails;
import com.example.utill.JWTUtill;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtill jwtTokenUtil;

    @PostConstruct
    @Transactional
    public void init() {
        User admin = new User("Admin","emailadmin@gmail.com", passwordEncoder.encode("passwordbroskiadmin"));
        admin.setRole(Role.ADMIN);
        checkIfUserExists(admin.getUsername());
        userRepository.save(admin);
    }

    @Transactional
    public void createUser(RegisterNewUserCommand createUserCommand) {
        String username = createUserCommand.getUsername();
        checkIfUserExists(username);
        String email = createUserCommand.getEmail();
        String password = passwordEncoder.encode(createUserCommand.getPassword());
        User defaultUser = new User(username, email, password);
        userRepository.save(defaultUser);
    }

    public Map<String, String> login(AuthorizationUserCommand authorizationUserCommand) {
        UsernamePasswordAuthenticationToken authInPutToken = new UsernamePasswordAuthenticationToken
                (authorizationUserCommand.getUsername(), authorizationUserCommand.getPassword());
        try {
            authenticationManager.authenticate(authInPutToken);

        } catch (BadCredentialsException e) {
            return Map.of("message", "incorrect credentials");
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authorizationUserCommand.getUsername());
        Role role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .orElseThrow(() -> new RoleNotFoundException("Role not found exception"));

        String token = jwtTokenUtil.generateToken(authorizationUserCommand.getUsername(), role);
        return Map.of("jwt-token", token);
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

    public User getCurrentUser() {
        String username = getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private String getAuthenticatedUsername() {
        UserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }


    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UpdateUserPasswordCommand updateUserCommand) {
        User user = getCurrentUser();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    protected void checkIfUserExists(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ThisUserAlreadyExists("This user already exists");
        }
    }
}

