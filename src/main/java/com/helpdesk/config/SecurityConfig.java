package com.helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // All: Switch to BCryptPasswordEncoder for production environments.
        // NoOpPasswordEncoder is used temporarily for local development.
        return NoOpPasswordEncoder.getInstance();
    }
}