package com.deakin.taskflow.service;

import com.deakin.taskflow.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // Default admin user for demonstration and testing
        users.add(new User(
                "user-1",
                "devops_lead",
                "devops@deakin.edu.au",
                passwordEncoder.encode("DevOpsPass123!"),
                "admin"
        ));
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username) || u.getEmail().equalsIgnoreCase(username))
                .findFirst();
    }

    public User registerUser(String username, String email, String password) {
        if (findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        User newUser = new User(
                UUID.randomUUID().toString(),
                username,
                email,
                passwordEncoder.encode(password),
                "developer"
        );
        users.add(newUser);
        return newUser;
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
