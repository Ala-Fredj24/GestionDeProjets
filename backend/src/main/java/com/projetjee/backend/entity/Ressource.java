package com.projetjee.backend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ressources")
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la ressource est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caracteres")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le type de ressource est obligatoire")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "Le cout est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le cout doit etre positif ou nul")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cout = BigDecimal.ZERO;

    @NotNull(message = "La disponibilite est obligatoire")
    @Column(nullable = false)
    private Boolean disponibilite = Boolean.TRUE;

    public Ressource() {
    }

    public Ressource(String nom, String type, BigDecimal cout, Boolean disponibilite) {
        this.nom = nom;
        this.type = type;
        this.cout = cout;
        this.disponibilite = disponibilite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCout() {
        return cout;
    }

    public void setCout(BigDecimal cout) {
        this.cout = cout;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }
}
