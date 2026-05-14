package com.projetjee.backend.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(
        name = "projets_ressources",
        uniqueConstraints = @UniqueConstraint(columnNames = {"projet_id", "ressource_id"})
)
public class ProjectRessource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le projet est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private Project projet;

    @NotNull(message = "La ressource est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ressource_id", nullable = false)
    private Ressource ressource;

    @NotNull(message = "La quantite est obligatoire")
    @Min(value = 1, message = "La quantite doit etre au moins 1")
    @Column(nullable = false)
    private Integer quantite = 1;

    @NotNull(message = "Le cout unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le cout unitaire doit etre positif ou nul")
    @Column(name = "cout_unitaire", nullable = false, precision = 12, scale = 2)
    private BigDecimal coutUnitaire = BigDecimal.ZERO;

    @Size(max = 255, message = "La note ne doit pas depasser 255 caracteres")
    @Column(length = 255)
    private String note;

    public ProjectRessource() {
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

    public Ressource getRessource() {
        return ressource;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(BigDecimal coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getCoutTotal() {
        BigDecimal unit = coutUnitaire != null ? coutUnitaire : BigDecimal.ZERO;
        int quantity = quantite != null ? quantite : 0;
        return unit.multiply(BigDecimal.valueOf(quantity));
    }
}
