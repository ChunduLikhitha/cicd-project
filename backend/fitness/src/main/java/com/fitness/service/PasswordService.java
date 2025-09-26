package com.fitness.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final BCryptPasswordEncoder passwordEncoder;

    // Use constructor instead of manual instantiation
    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    // Alternatively, you can use this constructor for better testing
    /*
    public PasswordService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    */

    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public boolean matches(String plainPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.trim().isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }
    
    // Optional: Add method to check password strength
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        // Add more strength checks as needed
        return true;
    }
}