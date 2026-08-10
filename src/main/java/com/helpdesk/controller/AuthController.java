package com.helpdesk.controller;

import com.helpdesk.dto.LoginRequest;
import com.helpdesk.dto.LoginResponse;
import com.helpdesk.dto.RegisterRequest;
import com.helpdesk.dto.UserResponse;
import com.helpdesk.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller responsible for user authentication and account registration endpoints
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor-based dependency injection for authentication business logic
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Handles new user registration requests
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Processes registration through service layer and returns sanitized UserResponse DTO
            UserResponse registeredUser = authService.register(registerRequest);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Catches validation errors such as duplicate email registrations
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            // General exception fallback for unexpected server failures
            return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Handles authentication requests and issues JWT Bearer tokens
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticates credentials and returns raw JWT token string
            String token = authService.login(loginRequest);

            // Wraps raw token into structured LoginResponse JSON payload
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (IllegalArgumentException e) {
            // Returns unauthorized response when credentials fail validation
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            // General exception fallback for login processing errors
            return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}