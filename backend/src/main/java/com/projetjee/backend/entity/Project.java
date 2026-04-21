package com.projetjee.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.projetjee.backend.enums.ProjectStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du projet est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name= "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name= "date_fin",nullable = false)
    private LocalDate dateFin;

    @NotNull(message = "Le budget est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le budget doit être supérieur à zéro")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus statut;

    public Project() {
    }

    public Project(Long id, String nom, LocalDate dateDebut, LocalDate dateFin, BigDecimal budget, ProjectStatus statut) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.budget = budget;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return nom;
    }

    public void setName(String nom) {
        this.nom = nom;
    }

    public LocalDate getStartDate() {
        return dateDebut;
    }

    public void setStartDate(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getEndDate() {
        return dateFin;
    }

    public void setEndDate(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public ProjectStatus getStatus() {
        return statut;
    }

    public void setStatus(ProjectStatus statut) {
        this.statut = statut;
    }
}