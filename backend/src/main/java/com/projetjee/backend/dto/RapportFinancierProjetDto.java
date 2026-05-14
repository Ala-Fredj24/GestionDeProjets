package com.projetjee.backend.dto;

import java.math.BigDecimal;

public class RapportFinancierProjetDto {
    private Long projetId;
    private String nomProjet;
    private String statutProjet;
    private BigDecimal budgetProjet;
    private BigDecimal coutPrevuTotal;
    private BigDecimal coutReelTotal;
    private BigDecimal coutTachesTotal;
    private BigDecimal coutRessourcesTotal;
    private BigDecimal coutGlobalTotal;
    private BigDecimal ecartPrevuReel;
    private BigDecimal resteBudget;
    private BigDecimal tauxConsommation;
    private boolean depasseBudget;
    private int nombreTaches;
    private int nombreTachesTerminees;
    private BigDecimal tauxAvancement;

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
        this.coutTachesTotal = coutReelTotal;
        this.coutRessourcesTotal = BigDecimal.ZERO;
        this.coutGlobalTotal = coutReelTotal;
        this.ecartPrevuReel = ecartPrevuReel;
        this.resteBudget = resteBudget;
        this.tauxConsommation = tauxConsommation;
        this.depasseBudget = depasseBudget;
        this.nombreTaches = nombreTaches;
        this.nombreTachesTerminees = 0;
        this.tauxAvancement = BigDecimal.ZERO;
    }

    public RapportFinancierProjetDto(Long projetId, String nomProjet, String statutProjet,
                                     BigDecimal budgetProjet, BigDecimal coutPrevuTotal, BigDecimal coutReelTotal,
                                     BigDecimal coutTachesTotal, BigDecimal coutRessourcesTotal,
                                     BigDecimal coutGlobalTotal, BigDecimal ecartPrevuReel,
                                     BigDecimal resteBudget, BigDecimal tauxConsommation,
                                     boolean depasseBudget, int nombreTaches) {
        this.projetId = projetId;
        this.nomProjet = nomProjet;
        this.statutProjet = statutProjet;
        this.budgetProjet = budgetProjet;
        this.coutPrevuTotal = coutPrevuTotal;
        this.coutReelTotal = coutReelTotal;
        this.coutTachesTotal = coutTachesTotal;
        this.coutRessourcesTotal = coutRessourcesTotal;
        this.coutGlobalTotal = coutGlobalTotal;
        this.ecartPrevuReel = ecartPrevuReel;
        this.resteBudget = resteBudget;
        this.tauxConsommation = tauxConsommation;
        this.depasseBudget = depasseBudget;
        this.nombreTaches = nombreTaches;
        this.nombreTachesTerminees = 0;
        this.tauxAvancement = BigDecimal.ZERO;
    }

    public RapportFinancierProjetDto(Long projetId, String nomProjet, String statutProjet,
                                     BigDecimal budgetProjet, BigDecimal coutPrevuTotal, BigDecimal coutReelTotal,
                                     BigDecimal coutTachesTotal, BigDecimal coutRessourcesTotal,
                                     BigDecimal coutGlobalTotal, BigDecimal ecartPrevuReel,
                                     BigDecimal resteBudget, BigDecimal tauxConsommation,
                                     boolean depasseBudget, int nombreTaches, int nombreTachesTerminees,
                                     BigDecimal tauxAvancement) {
        this(projetId, nomProjet, statutProjet, budgetProjet, coutPrevuTotal, coutReelTotal,
                coutTachesTotal, coutRessourcesTotal, coutGlobalTotal, ecartPrevuReel,
                resteBudget, tauxConsommation, depasseBudget, nombreTaches);
        this.nombreTachesTerminees = nombreTachesTerminees;
        this.tauxAvancement = tauxAvancement;
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

    public BigDecimal getCoutTachesTotal() {
        return coutTachesTotal;
    }

    public void setCoutTachesTotal(BigDecimal coutTachesTotal) {
        this.coutTachesTotal = coutTachesTotal;
    }

    public BigDecimal getCoutRessourcesTotal() {
        return coutRessourcesTotal;
    }

    public void setCoutRessourcesTotal(BigDecimal coutRessourcesTotal) {
        this.coutRessourcesTotal = coutRessourcesTotal;
    }

    public BigDecimal getCoutGlobalTotal() {
        return coutGlobalTotal;
    }

    public void setCoutGlobalTotal(BigDecimal coutGlobalTotal) {
        this.coutGlobalTotal = coutGlobalTotal;
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

    public int getNombreTachesTerminees() {
        return nombreTachesTerminees;
    }

    public void setNombreTachesTerminees(int nombreTachesTerminees) {
        this.nombreTachesTerminees = nombreTachesTerminees;
    }

    public BigDecimal getTauxAvancement() {
        return tauxAvancement;
    }

    public void setTauxAvancement(BigDecimal tauxAvancement) {
        this.tauxAvancement = tauxAvancement;
    }
     public BigDecimal getEcartPrevuReel() {
        return ecartPrevuReel;
    }

    public void setEcartPrevuReel(BigDecimal ecartPrevuReel) {
        this.ecartPrevuReel = ecartPrevuReel;
    }
}

