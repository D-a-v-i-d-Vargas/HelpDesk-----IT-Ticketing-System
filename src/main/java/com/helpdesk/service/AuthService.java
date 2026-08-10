package com.helpdesk.service;

import com.helpdesk.dto.LoginRequest;
import com.helpdesk.dto.RegisterRequest;
import com.helpdesk.dto.UserResponse;
import com.helpdesk.model.Role;
import com.helpdesk.model.User;
import com.helpdesk.repository.UserRepository;
import com.helpdesk.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service handling user authentication, credential validation, and account creation logic
@Service
public class AuthService {

    private static final Logger securityLogger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Constructor injection for required security and persistence dependencies
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Registers a new user account using data from RegisterRequest DTO
    public UserResponse register(RegisterRequest registerRequest) {
        securityLogger.info("SECURITY_EVENT: Registration attempt initiated for the email address: {}", registerRequest.getEmail());

        // Enforces password length security policy
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 8) {
            securityLogger.warn("SECURITY_ALERT: Registration denied. The password for the email address is too weak: {}", registerRequest.getEmail());
            throw new IllegalArgumentException("The password must be at least 8 characters long, in accordance with security policies.");
        }

        // Prevents duplicate account creation by checking email uniqueness
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            securityLogger.warn("SECURITY_ALERT: Attempt to register a duplicate email address: {}", registerRequest.getEmail());
            throw new IllegalArgumentException("The email address is already in use.");
        }

        // Constructs domain entity and encrypts raw password using BCrypt
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhoneNumber(registerRequest.getPhoneNumber());

        // Converts string role payload to Role enum with default fallback to EMPLOYEE
        try {
            user.setRole(Role.valueOf(registerRequest.getRole().toUpperCase()));
        } catch (Exception e) {
            user.setRole(Role.EMPLOYEE);
        }

        User savedUser = userRepository.save(user);
        securityLogger.info("SECURITY_EVENT: User successfully registered with ID: {}", savedUser.getUserId());

        // Returns sanitized DTO response without sensitive credentials
        return mapToUserResponse(savedUser);
    }

    // Authenticates user credentials and generates a signed JWT token
    public String login(LoginRequest loginRequest) {
        securityLogger.info("SECURITY_EVENT: Login attempt initiated for email: {}", loginRequest.getEmail());

        // Fetches target account or throws error if account does not exist
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    securityLogger.warn("SECURITY_ALERT: Login failed. User not found for email: {}", loginRequest.getEmail());
                    return new IllegalArgumentException("Invalid email or password.");
                });

        // Verifies raw password against stored BCrypt hash
        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            securityLogger.warn("SECURITY_ALERT: Login failed. Incorrect password for email: {}", loginRequest.getEmail());
            throw new IllegalArgumentException("Invalid email or password.");
        }

        securityLogger.info("SECURITY_EVENT: Successful login for user ID: {}", user.getUserId());

        // Issues JWT token containing subject email and user role
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    // Converts domain User entity into secure UserResponse DTO
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name()
        );
    }
}