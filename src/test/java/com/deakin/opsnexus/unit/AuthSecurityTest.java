package com.deakin.opsnexus.unit;

import com.deakin.opsnexus.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class AuthSecurityTest {

    private JwtTokenProvider jwtTokenProvider;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        String testSecret = "opsnexusdevopspipelinesupersecretkeythatislongerthan256bitsforhs256validation2026";
        jwtTokenProvider = new JwtTokenProvider(testSecret, 3600000);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void testBCryptPasswordHashingAndVerification() {
        String rawPassword = "SecureDevOpsPassword2026!";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
        assertFalse(passwordEncoder.matches("WrongPassword", encoded));
    }

    @Test
    void testJwtTokenGenerationAndValidation() {
        String token = jwtTokenProvider.generateToken("devops_user", "admin");

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("devops_user", jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void testJwtTokenRejectsTamperedToken() {
        String token = jwtTokenProvider.generateToken("devops_user", "admin");
        String tamperedToken = token + "xyz";

        assertFalse(jwtTokenProvider.validateToken(tamperedToken));
    }
}
