package com.helpdesk.controller;

import com.helpdesk.dto.UserResponse;
import com.helpdesk.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller managing user account lookup and profile management endpoints
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Constructor-based dependency injection for user management business logic
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retrieves a list of all registered users mapped to sanitized UserResponse DTOs
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Fetches a specific user profile by their unique database identifier
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Updates existing user profile details using provided DTO request payload
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserResponse userResponse) {
        return ResponseEntity.ok(userService.updateUser(id, userResponse));
    }
}
