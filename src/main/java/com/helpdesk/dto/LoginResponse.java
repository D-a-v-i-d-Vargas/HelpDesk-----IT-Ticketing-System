package com.helpdesk.dto;

// Structured payload returned upon successful authentication containing the JWT token
public class LoginResponse {

    private String token;
    private String type = "Bearer";

    // Default constructor required for JSON serialization libraries
    public LoginResponse() {
    }

    // Convenience constructor for standard Bearer token responses
    public LoginResponse(String token) {
        this.token = token;
    }

    // All-arguments constructor if a non-standard token type is needed
    public LoginResponse(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
