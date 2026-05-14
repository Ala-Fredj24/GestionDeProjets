package com.projetjee.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetjee.backend.dto.ProjectDetailsDto;
import com.projetjee.backend.service.ProjectDetailsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/projets")
@SecurityRequirement(name = "bearerAuth")
public class ProjectDetailsController {

    private final ProjectDetailsService projectDetailsService;

    public ProjectDetailsController(ProjectDetailsService projectDetailsService) {
        this.projectDetailsService = projectDetailsService;
    }

    @GetMapping("/{id}/details")
    public ProjectDetailsDto recupererDetailsProjet(@PathVariable Long id) {
        return projectDetailsService.recupererDetailsProjet(id);
    }
}
