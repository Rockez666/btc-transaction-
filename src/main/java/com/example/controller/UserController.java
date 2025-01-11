package com.example.controller;

import com.example.command.AuthResult;
import com.example.command.AuthorizationUserCommand;
import com.example.command.RegisterNewUserCommand;
import com.example.command.UpdateUserCommand;
import com.example.dto.UserDto;
import com.example.service.UserService;
import com.example.utill.JWTUtill;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTUtill jwtUtill;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegisterNewUserCommand registerCommand) {
        userService.createUser(registerCommand);

        String token = jwtUtill.generateToken(registerCommand.getUsername());


        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> performLogin(@RequestBody AuthorizationUserCommand authorizationUserCommand) {
        UsernamePasswordAuthenticationToken authInPutToken = new UsernamePasswordAuthenticationToken
                (authorizationUserCommand.getUsername(), authorizationUserCommand.getPassword());
        try {
            authenticationManager.authenticate(authInPutToken);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "incorrect credentials"));
        }
        String token = jwtUtill.generateToken(authorizationUserCommand.getUsername());
        return ResponseEntity.ok().body(Map.of("jwt-token", token));
    }

//    @GetMapping
//    public ResponseEntity<List<UserDto>> getUsers() {
//        return ResponseEntity.ok().body(userService.getAllUsers());
//    }


    @GetMapping("/showUserInfo")
    public ResponseEntity<String> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return  ResponseEntity.ok(userDetails.getUsername());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
//        UserDto userDto = userService.getUserDtoById(id);
//        return ResponseEntity.ok().body(userDto);
//    }
//
////    @GetMapping("/auth")
////    public ResponseEntity<String> auth(@Valid @RequestBody AuthorizationUserCommand authorizationCommand) {
////        AuthResult authResult = userService.auth(authorizationCommand);
////        return authResult.isSuccess() ? ResponseEntity.ok(authResult.getMessage()) : ResponseEntity.status(401).body(authResult.getMessage());
////    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUserByID(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok().body("User deleted");
//    }
//
//    @PutMapping()
//    public ResponseEntity<String> updateUserDetails(@Valid @RequestBody UpdateUserCommand updateCommand) {
//        userService.updateUser(updateCommand);
//        return ResponseEntity.ok().body("User updated");
//    }

}
