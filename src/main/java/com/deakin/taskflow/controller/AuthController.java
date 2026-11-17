package com.deakin.taskflow.controller;

import com.deakin.taskflow.model.AuthRequest;
import com.deakin.taskflow.model.AuthResponse;
import com.deakin.taskflow.model.User;
import com.deakin.taskflow.security.JwtTokenProvider;
import com.deakin.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        try {
            User user = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(true, "User registered successfully", token, user.getUsername(), user.getRole()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(false, ex.getMessage(), null, null, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());
        if (userOpt.isEmpty() || !userService.checkPassword(request.getPassword(), userOpt.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Invalid username or password", null, null, null));
        }

        User user = userOpt.get();
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(true, "Authentication successful", token, user.getUsername(), user.getRole()));
    }
}
