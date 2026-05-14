package com.projetjee.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.dto.RapportFinancierProjetDto;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Task;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.TaskRepository;

@Service
public class RapportFinancierService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectRessourceService projectRessourceService;

    public RapportFinancierService(ProjectRepository projectRepository, TaskRepository taskRepository,
            ProjectRessourceService projectRessourceService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectRessourceService = projectRessourceService;
    }

    public List<RapportFinancierProjetDto> chargerTousLesRapportsFinanciers() {
        return projectRepository.findAll()
                .stream()
                .map(this::construireRapportProjet)
                .toList();
    }

    public RapportFinancierProjetDto chargerRapportFinancierParProjet(Long projetId) {
        Project projet = projectRepository.findById(projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + projetId
                ));

        return construireRapportProjet(projet);
    }

    private RapportFinancierProjetDto construireRapportProjet(Project projet) {
        List<Task> taches = taskRepository.findByProjetId(projet.getId());

        BigDecimal budgetProjet = projet.getBudget() != null ? projet.getBudget() : BigDecimal.ZERO;

        BigDecimal coutReelTotal = taches.stream()
                .map(tache -> tache.getCoutReel() != null ? tache.getCoutReel() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal coutPrevuTotal = taches.stream()
                .map(tache -> tache.getCoutPrevu() != null ? tache.getCoutPrevu() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal coutRessourcesTotal = projectRessourceService.calculerCoutRessourcesProjet(projet.getId());
        BigDecimal coutEstimeTotal = coutPrevuTotal.add(coutRessourcesTotal);
        BigDecimal ecartPrevuReel = coutPrevuTotal.subtract(coutReelTotal);
        BigDecimal resteBudget = budgetProjet.subtract(coutEstimeTotal);

        BigDecimal tauxConsommation = BigDecimal.ZERO;
        if (budgetProjet.compareTo(BigDecimal.ZERO) > 0) {
            tauxConsommation = coutEstimeTotal
                    .multiply(new BigDecimal("100"))
                    .divide(budgetProjet, 2, RoundingMode.HALF_UP);
        }

        boolean depasseBudget = coutEstimeTotal.compareTo(budgetProjet) > 0;
        int nombreTachesTerminees = (int) taches.stream()
                .filter(tache -> tache.getStatut() != null && tache.getStatut().name().contains("Compl"))
                .count();
        BigDecimal tauxAvancement = BigDecimal.ZERO;
        if (!taches.isEmpty()) {
            tauxAvancement = BigDecimal.valueOf(nombreTachesTerminees)
                    .multiply(new BigDecimal("100"))
                    .divide(BigDecimal.valueOf(taches.size()), 2, RoundingMode.HALF_UP);
        }

        return new RapportFinancierProjetDto(
                projet.getId(),
                projet.getNom(),
                projet.getStatut().name(),
                budgetProjet,
                coutPrevuTotal,
                coutReelTotal,
                coutReelTotal,
                coutRessourcesTotal,
                coutEstimeTotal,
                ecartPrevuReel,
                resteBudget,
                tauxConsommation,
                depasseBudget,
                taches.size(),
                nombreTachesTerminees,
                tauxAvancement
        );
    }
}
