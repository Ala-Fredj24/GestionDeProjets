package com.projetjee.backend.entity;

import java.time.LocalDate;

import com.projetjee.backend.converter.TaskPriorityConverter;
import com.projetjee.backend.converter.TaskStatusConverter;
import com.projetjee.backend.enums.TaskPriority;
import com.projetjee.backend.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "taches")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le projet est obligatoire")
    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Project projet;

    @NotBlank(message = "Le responsable est obligatoire")
    @Column(nullable = false)
    private String responsable;

    @NotBlank(message = "La description est obligatoire")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Le statut est obligatoire")
    @Convert(converter = TaskStatusConverter.class)
    @Column(nullable = false)
    private TaskStatus statut;

    @NotNull(message = "La priorité est obligatoire")
    @Convert(converter = TaskPriorityConverter.class)
    @Column(nullable = false)
    private TaskPriority priorite;

    @NotNull(message = "La date limite est obligatoire")
    @FutureOrPresent(message = "La date limite doit être aujourd'hui ou dans le futur")
    @Column(name = "date_limite", nullable = false)
    private LocalDate dateLimite;

    public Task() {
    }

    public Task(Project projet, String responsable, String description, TaskStatus statut, LocalDate dateLimite, TaskPriority priorite) {
        this.projet = projet;
        this.responsable = responsable;
        this.description = description;
        this.statut = statut;
        this.dateLimite = dateLimite;
        this.priorite = priorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProjet() {
        return projet;
    }

    public void setProjet(Project projet) {
        this.projet = projet;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatut() {
        return statut;
    }

    public void setStatut(TaskStatus statut) {
        this.statut = statut;
    }

    public TaskPriority getPriorite() {
        return priorite;
    }

    public void setPriorite(TaskPriority priorite) {
        this.priorite = priorite;
    }

    public LocalDate getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(LocalDate dateLimite) {
        this.dateLimite = dateLimite;
    }
}
