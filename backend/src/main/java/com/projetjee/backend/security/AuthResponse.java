package com.projetjee.backend.security;

public class AuthResponse {

    private String token;
    private String email;
    private String role;
    private Long employeeId;

    public AuthResponse() {
    }

    public AuthResponse(String token, String email, String role, Long employeeId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.employeeId = employeeId;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}