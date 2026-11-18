package com.deakin.opsnexus.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication Response Payload")
public class AuthResponse {

    @Schema(description = "Operation success flag", example = "true")
    private boolean success;

    @Schema(description = "Informative status message", example = "Authentication successful")
    private String message;

    @Schema(description = "JWT Bearer token for accessing protected endpoints", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Authenticated username", example = "devops_lead")
    private String username;

    @Schema(description = "Assigned user role", example = "admin")
    private String role;

    public AuthResponse() {}

    public AuthResponse(boolean success, String message, String token, String username, String role) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
