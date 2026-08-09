package com.helpdesk.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== STARTING SECURITY FILTER ===");
        System.out.println("Requested URI: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        System.out.println("Received Authorization Header: " + authHeader);

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
                System.out.println(" User extracted from token: " + username);
            } catch (Exception e) {
                System.out.println(" Error extracting user: " + e.getMessage());
            }
        } else {
            System.out.println(" No 'Bearer ' prefix found in the header.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            boolean isValid = jwtUtil.validateToken(token);
            System.out.println(" Is the token mathematically valid?: " + isValid);

            if (isValid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println(" Access granted for Spring Security.");
            } else {
                System.out.println(" Token validation failed.");
            }
        }

        System.out.println("=========================================");
        filterChain.doFilter(request, response);
    }
}