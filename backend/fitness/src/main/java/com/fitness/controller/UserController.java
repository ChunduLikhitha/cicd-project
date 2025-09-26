// src/main/java/com/fitness/controller/UserController.java
package com.fitness.controller;

import com.fitness.model.User;
import com.fitness.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, allowedHeaders = "*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> test() {
        logger.info("Received request to /api/users/test");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backend is running");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody User user) {
        logger.info("Received signup request for username: {}", user.getUsername());
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                logger.warn("Signup failed: Username is required");
                return ResponseEntity.badRequest().body(createErrorResponse("Username is required"));
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                logger.warn("Signup failed: Email is required");
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }
            if (user.getPassword() == null || user.getPassword().length() < 6) {
                logger.warn("Signup failed: Password must be at least 6 characters");
                return ResponseEntity.badRequest().body(createErrorResponse("Password must be at least 6 characters"));
            }

            User savedUser = userService.register(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", Map.of(
                "id", savedUser.getId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail()
            ));
            logger.info("User registered successfully: {}", savedUser.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Signup error: {}", e.getMessage());
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(createErrorResponse(e.getMessage()));
            }
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during signup: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred during registration"));
        }
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for username: {}", loginRequest.getUsername());
        try {
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                logger.warn("Login failed: Username is required");
                return ResponseEntity.badRequest().body(createErrorResponse("Username is required"));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                logger.warn("Login failed: Password is required");
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }

            Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail()
                ));
                logger.info("User logged in successfully: {}", user.getUsername());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Login failed: Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Invalid username or password"));
            }
        } catch (Exception e) {
            logger.error("Unexpected error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred during login"));
        }
    }

    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        errorResponse.put("success", false);
        return errorResponse;
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}