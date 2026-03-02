package com.ricardo.dto;

public class RegisterRequest {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public RegisterRequest() {};

    public String getUsername() {
        return username;
    }
}
