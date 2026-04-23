package com.projetjee.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.projetjee.backend.converter.ProjectStatusConverter;
import com.projetjee.backend.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @NotNull(message = "Le budget est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le budget doit être supérieur à zéro")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;

    @NotNull(message = "Le statut est obligatoire")
    @Convert(converter = ProjectStatusConverter.class)
    @Column(nullable = false)
    private ProjectStatus statut;
    @ManyToMany
    @JoinTable(name = "projets_employes", joinColumns = @JoinColumn(name = "projet_id"), inverseJoinColumns = @JoinColumn(name = "employe_id"))
    private List<Employee> employes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "chef_projet_id")
    private Employee chefProjet;

    public Project() {
    }

    public Project(Long id, String nom, LocalDate dateDebut, LocalDate dateFin, BigDecimal budget,
            ProjectStatus statut) {
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public ProjectStatus getStatut() {
        return statut;
    }

    public void setStatut(ProjectStatus statut) {
        this.statut = statut;
    }

    public List<Employee> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Employee> employes) {
        this.employes = employes;
    }
    
    public Employee getChefProjet() {
        return chefProjet;
    }

    public void setChefProjet(Employee chefProjet) {
        this.chefProjet = chefProjet;
    }
}
