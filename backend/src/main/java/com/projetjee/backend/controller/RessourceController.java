package com.projetjee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetjee.backend.entity.Ressource;
import com.projetjee.backend.service.RessourceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ressources")
@CrossOrigin(origins = "http://localhost:4200")
@SecurityRequirement(name = "bearerAuth")
public class RessourceController {

    private final RessourceService ressourceService;

    public RessourceController(RessourceService ressourceService) {
        this.ressourceService = ressourceService;
    }

    @Operation(summary = "Lister toutes les ressources dediees")
    @GetMapping
    public List<Ressource> recupererToutesLesRessources() {
        return ressourceService.recupererToutesLesRessources();
    }

    @Operation(summary = "Recuperer une ressource par son identifiant")
    @GetMapping("/{id}")
    public Ressource recupererRessourceParId(@PathVariable Long id) {
        return ressourceService.recupererRessourceParId(id);
    }

    @Operation(summary = "Creer une ressource dediee")
    @PostMapping
    public ResponseEntity<Ressource> creerRessource(@Valid @RequestBody Ressource ressource) {
        Ressource createdRessource = ressourceService.creerRessource(ressource);
        return new ResponseEntity<>(createdRessource, HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre a jour une ressource dediee")
    @PutMapping("/{id}")
    public ResponseEntity<Ressource> mettreAJourRessource(@PathVariable Long id,
            @Valid @RequestBody Ressource ressource) {
        Ressource updatedRessource = ressourceService.mettreAJourRessource(id, ressource);
        return ResponseEntity.ok(updatedRessource);
    }

    @Operation(summary = "Supprimer une ressource dediee")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerRessource(@PathVariable Long id) {
        ressourceService.supprimerRessource(id);
        return ResponseEntity.noContent().build();
    }
}
