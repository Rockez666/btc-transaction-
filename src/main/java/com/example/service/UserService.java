package com.example.service;

import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.enums.Role;
import com.example.exception.InvalidPasswordException;
import com.example.exception.RoleNotFoundException;
import com.example.exception.ThisUsernameOrEmailAlreadyExists;
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
    private final EmailService emailService;


    @PostConstruct
    @Transactional
    public void init() {
        User admin = new User("Admin","emailadmin@gmail.com","777", passwordEncoder.encode("passwordbroskiadmin"));
        admin.setRole(Role.ADMIN);
        checkIfUsernameOrEmailExists(admin.getUsername());
        userRepository.save(admin);
    }

    @Transactional
    public void createUser(RegisterNewUserCommand createUserCommand) {
        String username = createUserCommand.getUsername();
        checkIfUsernameOrEmailExists(username);
        String email = createUserCommand.getEmail();
        String verificationCode = VerificationCodeGenerator.generateVerificationCode();
        String password = passwordEncoder.encode(createUserCommand.getPassword());
        User newUser = new User(username, email,verificationCode, password);
        emailService.sendVerificationEmail(email, verificationCode);
    }

    public Map<String, String> login(AuthorizationUserCommand authorizationUserCommand) {
        UsernamePasswordAuthenticationToken authInPutToken = new UsernamePasswordAuthenticationToken
                (authorizationUserCommand.getUsername(), authorizationUserCommand.getPassword());
        try {
            authenticationManager.authenticate(authInPutToken);

        } catch (BadCredentialsException e) {
            throw new InvalidPasswordException("Invalid credentials");
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


//
//    @Transactional
//    public void updateUser(UpdateUserPasswordCommand updateUserCommand) {
//        User authUser = getCurrentUser();
//
//        userRepository.save(user);
//    }

    @Transactional(readOnly = true)
    private void checkIfUsernameOrEmailExists(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ThisUsernameOrEmailAlreadyExists("This username or email already exists");
        }
    }
}

