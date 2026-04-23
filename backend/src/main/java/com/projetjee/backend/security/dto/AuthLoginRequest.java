package com.projetjee.backend.security.dto;

public class AuthLoginRequest {

    private String email;
    private String motDePasse;

    public AuthLoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}