package com.projetjee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetjee.backend.dto.ProjectRessourceRequest;
import com.projetjee.backend.dto.ProjectRessourceResponse;
import com.projetjee.backend.service.ProjectRessourceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projets/{projetId}/ressources")
@SecurityRequirement(name = "bearerAuth")
public class ProjectRessourceController {

    private final ProjectRessourceService projectRessourceService;

    public ProjectRessourceController(ProjectRessourceService projectRessourceService) {
        this.projectRessourceService = projectRessourceService;
    }

    @GetMapping
    public List<ProjectRessourceResponse> recupererRessourcesProjet(@PathVariable Long projetId) {
        return projectRessourceService.recupererRessourcesProjet(projetId);
    }

    @PostMapping
    public ResponseEntity<ProjectRessourceResponse> ajouterRessourceAuProjet(@PathVariable Long projetId,
            @Valid @RequestBody ProjectRessourceRequest request) {
        ProjectRessourceResponse response = projectRessourceService.ajouterRessourceAuProjet(projetId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{affectationId}")
    public ResponseEntity<ProjectRessourceResponse> modifierRessourceProjet(@PathVariable Long projetId,
            @PathVariable Long affectationId, @Valid @RequestBody ProjectRessourceRequest request) {
        return ResponseEntity.ok(projectRessourceService.modifierRessourceProjet(projetId, affectationId, request));
    }

    @DeleteMapping("/{affectationId}")
    public ResponseEntity<Void> supprimerRessourceProjet(@PathVariable Long projetId,
            @PathVariable Long affectationId) {
        projectRessourceService.supprimerRessourceProjet(projetId, affectationId);
        return ResponseEntity.noContent().build();
    }
}
