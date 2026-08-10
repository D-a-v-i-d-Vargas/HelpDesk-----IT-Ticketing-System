package com.helpdesk.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

// Custom filter executed once per HTTP request to validate incoming JWT Bearer tokens
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger filterLogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    // Constructor injection for JWT utility component
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extracts Authorization header from the incoming HTTP request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Verifies presence of Bearer scheme before attempting token extraction
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                filterLogger.warn("JWT token parsing failed or token expired: {}", e.getMessage());
            }
        }

        // Authenticates request if valid token username exists and context is not yet populated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            boolean isValid = jwtUtil.validateToken(token);

            if (isValid) {
                // Sets authentication token in Spring Security Context to grant endpoint access
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterLogger.debug("Successfully authenticated user: {} via JWT", username);
            } else {
                filterLogger.warn("JWT token validation failed for user: {}", username);
            }
        }

        // Passes request down the filter chain
        filterChain.doFilter(request, response);
    }
}