package com.helpdesk.repository;

import com.helpdesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository interface managing database persistence operations for User entities
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method retrieving a User account by unique email address
    Optional<User> findByEmail(String email);
}