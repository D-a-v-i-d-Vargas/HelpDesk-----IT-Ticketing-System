package com.helpdesk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Diagnostic controller used to verify protected endpoints and JWT authorization
@RestController
@RequestMapping("/api/test")
public class TestController {

    // Secured endpoint requiring a valid JWT Bearer token in the Authorization header
    @GetMapping("/hello")
    public ResponseEntity<String> secureGreeting() {
        // Confirms request successfully passed through JwtAuthenticationFilter
        return ResponseEntity.ok("Mission accomplished! If you see this, your JWT token is working.");
    }
}