package com.projetjee.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Task;

public class ProjectDetailsDto {
    private Project projet;
    private List<Task> taches;
    private List<ProjectRessourceResponse> ressources;
    private List<Employee> employes;
    private BigDecimal coutTachesPrevu;
    private BigDecimal coutTachesReel;
    private BigDecimal coutRessources;
    private BigDecimal coutGlobal;

    public ProjectDetailsDto(Project projet, List<Task> taches, List<ProjectRessourceResponse> ressources,
            List<Employee> employes, BigDecimal coutTachesPrevu, BigDecimal coutTachesReel,
            BigDecimal coutRessources, BigDecimal coutGlobal) {
        this.projet = projet;
        this.taches = taches;
        this.ressources = ressources;
        this.employes = employes;
        this.coutTachesPrevu = coutTachesPrevu;
        this.coutTachesReel = coutTachesReel;
        this.coutRessources = coutRessources;
        this.coutGlobal = coutGlobal;
    }

    public Project getProjet() {
        return projet;
    }

    public List<Task> getTaches() {
        return taches;
    }

    public List<ProjectRessourceResponse> getRessources() {
        return ressources;
    }

    public List<Employee> getEmployes() {
        return employes;
    }

    public BigDecimal getCoutTachesPrevu() {
        return coutTachesPrevu;
    }

    public BigDecimal getCoutTachesReel() {
        return coutTachesReel;
    }

    public BigDecimal getCoutRessources() {
        return coutRessources;
    }

    public BigDecimal getCoutGlobal() {
        return coutGlobal;
    }
}
