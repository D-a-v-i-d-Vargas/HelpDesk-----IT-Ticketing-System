package com.helpdesk.service;

import com.helpdesk.dto.LoginRequest;
import com.helpdesk.model.User;
import com.helpdesk.repository.UserRepository;
import com.helpdesk.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger securityLogger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(User user){

        securityLogger.info("SECURITY_EVENT: Registration attempt initiated for the email address: {}", user.getEmail());

        if (user.getPassword() == null || user.getPassword().length() < 8){
            securityLogger.warn("SECURITY_ALERT: Registration denied. The password for the email address is too weak: {}", user.getEmail());
            throw new IllegalArgumentException("The password must be at least 8 characters long, in accordance with security policies.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            securityLogger.warn("SECURITY_ALERT: Attempt to register a duplicate email address: {}", user.getEmail());
            throw new IllegalArgumentException("The email address is already in use.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        securityLogger.info("SECURITY_EVENT: User successfully registered with ID: {}", user.getUserId());

        return savedUser;
    }

    public String login(LoginRequest loginRequest){

        securityLogger.info("SECURITY_EVENT: Login attempt initiated for email: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    securityLogger.warn("SECURITY_ALERT: Login failed. User not found for email: {}", loginRequest.getEmail());
                    return new IllegalArgumentException("Invalid email or password.");
                });

        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            securityLogger.warn("SECURITY_ALERT: Login failed. Incorrect password for email: {}", loginRequest.getEmail());
            throw new IllegalArgumentException("Invalid email or password.");
        }

        securityLogger.info("SECURITY_EVENT: Successful login for user ID: {}", user.getUserId());

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return token;
    }
}