package com.projetjee.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Project;
import com.projetjee.backend.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> recupererTousLesProjets() {
        return projectRepository.findAll();
    }

    public Project recupererProjetParId(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + id
                ));
    }

    public Project creerProjet(Project project) {
        validerDatesProjet(project);
        return projectRepository.save(project);
    }

    public Project mettreAJourProjet(Long id, Project projectDetails) {
        Project existingProject = recupererProjetParId(id);

        validerDatesProjet(projectDetails);

        existingProject.setNom(projectDetails.getNom());
        existingProject.setDateDebut(projectDetails.getDateDebut());
        existingProject.setDateFin(projectDetails.getDateFin());
        existingProject.setBudget(projectDetails.getBudget());
        existingProject.setStatut(projectDetails.getStatut());

        return projectRepository.save(existingProject);
    }

    public void supprimerProjet(Long id) {
        Project existingProject = recupererProjetParId(id);
        projectRepository.delete(existingProject);
    }

    private void validerDatesProjet(Project project) {
        if (project.getDateDebut() != null
                && project.getDateFin() != null
                && !project.getDateDebut().isBefore(project.getDateFin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date de début doit être antérieure à la date de fin."
            );
        }
    }
}