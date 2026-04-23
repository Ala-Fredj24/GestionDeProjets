package com.projetjee.backend.security;

import com.projetjee.backend.entity.Employee;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employe;

    public User() {
    }

    public User(String email, String motDePasse, Role role, Employee employe) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.employe = employe;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public Employee getEmploye() {
        return employe;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmploye(Employee employe) {
        this.employe = employe;
    }
}