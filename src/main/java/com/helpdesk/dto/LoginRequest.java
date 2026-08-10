package com.helpdesk.dto;

// Payload structure for authentication requests
public class LoginRequest {

    private String email;
    private String password;

    // Default constructor required by Jackson for request body deserialization
    public LoginRequest() {
    }

    // All-arguments constructor useful for unit test setups
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
