package com.projetjee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projetjee.backend.entity.Project;
import com.projetjee.backend.service.ProjectService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projets")
@CrossOrigin(origins = "http://localhost:4200")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> recupererTousLesProjets() {
        return projectService.recupererTousLesProjets();
    }

    @GetMapping("/{id}")
    public Project recupererProjetParId(@PathVariable Long id) {
        return projectService.recupererProjetParId(id);
    }

    @PostMapping
    public ResponseEntity<Project> creerProjet(@Valid @RequestBody Project project) {
        Project createdProject = projectService.creerProjet(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> mettreAJourProjet(@PathVariable Long id, @Valid @RequestBody Project project) {
        Project updatedProject = projectService.mettreAJourProjet(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProjet(@PathVariable Long id) {
        projectService.supprimerProjet(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/employes")
    public ResponseEntity<Project> affecterEmployesAuProjet(@PathVariable Long id, @RequestBody List<Long> employeIds) {
        Project updatedProject = projectService.affecterEmployesAuProjet(id, employeIds);
        return ResponseEntity.ok(updatedProject);
    }
}