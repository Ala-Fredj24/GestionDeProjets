package com.projetjee.backend.security;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projetjee.backend.entity.Project;
import com.projetjee.backend.repository.ProjectRepository;

@RestController
@RequestMapping("/api/chef")
@CrossOrigin(origins = "http://localhost:4200")
public class ChefProjetController {

    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;

    public ChefProjetController(CurrentUserService currentUserService, ProjectRepository projectRepository) {
        this.currentUserService = currentUserService;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projets")
    public List<Project> recupererMesProjets() {
        User user = currentUserService.getCurrentUser();

        if (user.getEmploye() == null || user.getEmploye().getId() == null) {
            return List.of();
        }

        return projectRepository.findVisibleProjectsForEmployee(user.getEmploye().getId());
    }
}