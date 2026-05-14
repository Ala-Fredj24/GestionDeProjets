package com.projetjee.backend.dto;

import java.math.BigDecimal;

public class ProjectRessourceResponse {
    private Long id;
    private Long projetId;
    private Long ressourceId;
    private String nomRessource;
    private String typeRessource;
    private Boolean disponibilite;
    private Integer quantite;
    private BigDecimal coutUnitaire;
    private BigDecimal coutTotal;
    private String note;

    public ProjectRessourceResponse() {
    }

    public ProjectRessourceResponse(Long id, Long projetId, Long ressourceId, String nomRessource,
            String typeRessource, Boolean disponibilite, Integer quantite, BigDecimal coutUnitaire,
            BigDecimal coutTotal, String note) {
        this.id = id;
        this.projetId = projetId;
        this.ressourceId = ressourceId;
        this.nomRessource = nomRessource;
        this.typeRessource = typeRessource;
        this.disponibilite = disponibilite;
        this.quantite = quantite;
        this.coutUnitaire = coutUnitaire;
        this.coutTotal = coutTotal;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public Long getProjetId() {
        return projetId;
    }

    public Long getRessourceId() {
        return ressourceId;
    }

    public String getNomRessource() {
        return nomRessource;
    }

    public String getTypeRessource() {
        return typeRessource;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public BigDecimal getCoutUnitaire() {
        return coutUnitaire;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public String getNote() {
        return note;
    }
}
