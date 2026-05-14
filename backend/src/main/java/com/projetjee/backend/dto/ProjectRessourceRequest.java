package com.projetjee.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProjectRessourceRequest {

    @NotNull(message = "La ressource est obligatoire")
    private Long ressourceId;

    @NotNull(message = "La quantite est obligatoire")
    @Min(value = 1, message = "La quantite doit etre au moins 1")
    private Integer quantite;

    @NotNull(message = "Le cout unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le cout unitaire doit etre positif ou nul")
    private BigDecimal coutUnitaire;

    @Size(max = 255, message = "La note ne doit pas depasser 255 caracteres")
    private String note;

    public Long getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Long ressourceId) {
        this.ressourceId = ressourceId;
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
}
