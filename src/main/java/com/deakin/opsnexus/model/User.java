package com.deakin.opsnexus.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OpsNexus User Entity")
public class User {

    @Schema(description = "Unique user ID", example = "user-1")
    private String id;

    @Schema(description = "Username", example = "devops_lead")
    private String username;

    @Schema(description = "Email address", example = "devops@deakin.edu.au")
    private String email;

    private String passwordHash;

    @Schema(description = "Assigned user role", example = "admin")
    private String role;

    public User() {}

    public User(String id, String username, String email, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
