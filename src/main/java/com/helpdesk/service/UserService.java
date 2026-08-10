package com.helpdesk.service;

import com.helpdesk.dto.UserResponse;
import com.helpdesk.model.User;
import com.helpdesk.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Service handling user account queries and profile updates
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection for database repository access
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieves all user records and converts entities to UserResponse DTOs
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    // Fetches single user account by database identifier or throws exception
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    // Modifies user profile details and persists updates to database
    public UserResponse updateUser(Long id, UserResponse userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhoneNumber(userDetails.getPhoneNumber());

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    // Utility method mapping internal User model to external UserResponse DTO
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
