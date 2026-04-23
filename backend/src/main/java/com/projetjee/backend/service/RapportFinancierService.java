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

    public RapportFinancierService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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

        BigDecimal ecartPrevuReel = coutPrevuTotal.subtract(coutReelTotal);     
        BigDecimal resteBudget = budgetProjet.subtract(coutReelTotal);

        BigDecimal tauxConsommation = BigDecimal.ZERO;
        if (budgetProjet.compareTo(BigDecimal.ZERO) > 0) {
            tauxConsommation = coutReelTotal
                    .multiply(new BigDecimal("100"))
                    .divide(budgetProjet, 2, RoundingMode.HALF_UP);
        }

        boolean depasseBudget = coutReelTotal.compareTo(budgetProjet) > 0;

        return new RapportFinancierProjetDto(
                projet.getId(),
                projet.getNom(),
                projet.getStatut().name(),
                budgetProjet,
                ecartPrevuReel,
                coutPrevuTotal,
                coutReelTotal,
                resteBudget,
                tauxConsommation,
                depasseBudget,
                taches.size()
        );
    }
}
