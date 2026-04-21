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

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + id
                ));
    }

    public Project createProject(Project project) {
        validateProjectDates(project);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project existingProject = getProjectById(id);

        validateProjectDates(projectDetails);

        existingProject.setName(projectDetails.getName());
        existingProject.setStartDate(projectDetails.getStartDate());
        existingProject.setEndDate(projectDetails.getEndDate());
        existingProject.setBudget(projectDetails.getBudget());
        existingProject.setStatus(projectDetails.getStatus());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long id) {
        Project existingProject = getProjectById(id);
        projectRepository.delete(existingProject);
    }

    private void validateProjectDates(Project project) {
        if (project.getStartDate() != null
                && project.getEndDate() != null
                && !project.getStartDate().isBefore(project.getEndDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La date de début doit être antérieure à la date de fin."
            );
        }
    }
}