package com.projetjee.backend.dto;

import java.math.BigDecimal;

public class RapportFinancierProjetDto {
    private Long projetId;
    private String nomProjet;
    private String statutProjet;
    private BigDecimal budgetProjet;
    private BigDecimal coutPrevuTotal;
    private BigDecimal coutReelTotal;
    private BigDecimal ecartPrevuReel;
    private BigDecimal resteBudget;
    private BigDecimal tauxConsommation;
    private boolean depasseBudget;
    private int nombreTaches;

    public RapportFinancierProjetDto() {
    }

    public RapportFinancierProjetDto(Long projetId, String nomProjet, String statutProjet,
                                     BigDecimal budgetProjet, BigDecimal coutPrevuTotal, BigDecimal coutReelTotal,
                                     BigDecimal ecartPrevuReel, BigDecimal resteBudget, BigDecimal tauxConsommation,
                                     boolean depasseBudget, int nombreTaches) {
        this.projetId = projetId;
        this.nomProjet = nomProjet;
        this.statutProjet = statutProjet;
        this.budgetProjet = budgetProjet;
        this.coutPrevuTotal = coutPrevuTotal;
        this.coutReelTotal = coutReelTotal;
        this.ecartPrevuReel = ecartPrevuReel;
        this.resteBudget = resteBudget;
        this.tauxConsommation = tauxConsommation;
        this.depasseBudget = depasseBudget;
        this.nombreTaches = nombreTaches;
    }

    public Long getProjetId() {
        return projetId;
    }

    public void setProjetId(Long projetId) {
        this.projetId = projetId;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getStatutProjet() {
        return statutProjet;
    }

    public void setStatutProjet(String statutProjet) {
        this.statutProjet = statutProjet;
    }

    public BigDecimal getBudgetProjet() {
        return budgetProjet;
    }

    public void setBudgetProjet(BigDecimal budgetProjet) {
        this.budgetProjet = budgetProjet;
    }

    public BigDecimal getCoutPrevuTotal() {
        return coutPrevuTotal;
    }

    public void setCoutPrevuTotal(BigDecimal coutPrevuTotal) {
        this.coutPrevuTotal = coutPrevuTotal;
    }

    public BigDecimal getCoutReelTotal() {
        return coutReelTotal;
    }

    public void setCoutReelTotal(BigDecimal coutReelTotal) {
        this.coutReelTotal = coutReelTotal;
    }

    public BigDecimal getResteBudget() {
        return resteBudget;
    }

    public void setResteBudget(BigDecimal resteBudget) {
        this.resteBudget = resteBudget;
    }

    public BigDecimal getTauxConsommation() {
        return tauxConsommation;
    }

    public void setTauxConsommation(BigDecimal tauxConsommation) {
        this.tauxConsommation = tauxConsommation;
    }

    public boolean isDepasseBudget() {
        return depasseBudget;
    }

    public void setDepasseBudget(boolean depasseBudget) {
        this.depasseBudget = depasseBudget;
    }

    public int getNombreTaches() {
        return nombreTaches;
    }

    public void setNombreTaches(int nombreTaches) {
        this.nombreTaches = nombreTaches;
    }
     public BigDecimal getEcartPrevuReel() {
        return ecartPrevuReel;
    }

    public void setEcartPrevuReel(BigDecimal ecartPrevuReel) {
        this.ecartPrevuReel = ecartPrevuReel;
    }
}

