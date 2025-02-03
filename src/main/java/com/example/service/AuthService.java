package com.example.service;

import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.entity.User;
import com.example.enums.Role;
import com.example.exception.*;
import com.example.repository.UserRepository;
import com.example.utill.JWTUtill;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtill jwtTokenUtil;


    @PostConstruct
    @Transactional
    public void init() {
        User admin = new User("Admin", "emailadmin@gmail.com", passwordEncoder.encode("passwordbroskiadmin"),"555");
        admin.setRole(Role.ADMIN);
        checkIfUsernameOrEmailExists(admin.getUsername(), admin.getEmail());
        userRepository.save(admin);
    }


    @Transactional
    public void createUser(RegisterNewUserCommand createUserCommand) {
        String username = createUserCommand.getUsername();
        String email = createUserCommand.getEmail();
        checkIfUsernameOrEmailExists(username, email);
        String verificationCode = emailService.generateCode();
        String password = passwordEncoder.encode(createUserCommand.getPassword());
        User newUser = new User(username, email, password,verificationCode);
        userRepository.save(newUser);
        emailService.sendVerificationEmail(email,verificationCode);
    }

    public String login(AuthorizationUserCommand authorizationUserCommand) {
        UsernamePasswordAuthenticationToken authInPutToken = new UsernamePasswordAuthenticationToken
                (authorizationUserCommand.getUsername(), authorizationUserCommand.getPassword());
        try {
            authenticationManager.authenticate(authInPutToken);

        } catch (BadCredentialsException e) {
            throw new InvalidPasswordException("Invalid credentials");
        }
        UserDetails userDetails =  customUserDetailsService.loadUserByUsername(authorizationUserCommand.getUsername());
        Role role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .orElseThrow(() -> new RoleNotFoundException("Role not found exception"));

        return jwtTokenUtil.generateLoginToken(authorizationUserCommand.getUsername(), role);
    }

    public void sendLinkToResetPassword(String email) {
        User userToReset = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        userToReset.setVerificationCode(null);
        String verificationCode = emailService.generateCode();
        userToReset.setVerificationCode(verificationCode);
        emailService.sendVerificationLinkToResetPassword(email,verificationCode);
    }

    public void passwordReset(String email,String newPassword) {

    }


    private void checkIfUsernameOrEmailExists(String username, String email) {
        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            throw new ThisUsernameOrEmailAlreadyExists("This username or email already exists");
        }
    }

}
