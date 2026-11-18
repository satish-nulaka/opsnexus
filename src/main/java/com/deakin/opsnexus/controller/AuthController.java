package com.deakin.opsnexus.controller;

import com.deakin.opsnexus.model.AuthRequest;
import com.deakin.opsnexus.model.AuthResponse;
import com.deakin.opsnexus.model.User;
import com.deakin.opsnexus.security.JwtTokenProvider;
import com.deakin.opsnexus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Management", description = "Endpoints for user registration and JWT token authentication")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new developer user", description = "Creates a new user profile with encrypted password and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists with this username/email"),
            @ApiResponse(responseCode = "400", description = "Validation error on required fields")
    })
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
    @Operation(summary = "Authenticate user credentials", description = "Validates username and password, returning an HMAC-SHA256 signed JWT Bearer token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided")
    })
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
