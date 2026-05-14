package com.projetjee.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projetjee.backend.dto.ProjectDetailsDto;
import com.projetjee.backend.dto.ProjectRessourceResponse;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Task;
import com.projetjee.backend.repository.TaskRepository;

@Service
public class ProjectDetailsService {

    private final ProjectService projectService;
    private final TaskRepository taskRepository;
    private final ProjectRessourceService projectRessourceService;

    public ProjectDetailsService(ProjectService projectService, TaskRepository taskRepository,
            ProjectRessourceService projectRessourceService) {
        this.projectService = projectService;
        this.taskRepository = taskRepository;
        this.projectRessourceService = projectRessourceService;
    }

    public ProjectDetailsDto recupererDetailsProjet(Long projetId) {
        Project projet = projectService.recupererProjetParId(projetId);
        List<Task> taches = taskRepository.findByProjetId(projetId);
        List<ProjectRessourceResponse> ressources = projectRessourceService.recupererRessourcesProjet(projetId);

        BigDecimal coutTachesPrevu = taches.stream()
                .map(tache -> tache.getCoutPrevu() != null ? tache.getCoutPrevu() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal coutTachesReel = taches.stream()
                .map(tache -> tache.getCoutReel() != null ? tache.getCoutReel() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal coutRessources = ressources.stream()
                .map(ressource -> ressource.getCoutTotal() != null ? ressource.getCoutTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ProjectDetailsDto(
                projet,
                taches,
                ressources,
                projet.getEmployes(),
                coutTachesPrevu,
                coutTachesReel,
                coutRessources,
                coutTachesPrevu.add(coutRessources)
        );
    }
}
