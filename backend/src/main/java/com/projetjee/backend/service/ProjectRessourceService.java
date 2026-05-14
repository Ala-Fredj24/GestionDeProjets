package com.projetjee.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.dto.ProjectRessourceRequest;
import com.projetjee.backend.dto.ProjectRessourceResponse;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.ProjectRessource;
import com.projetjee.backend.entity.Ressource;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.ProjectRessourceRepository;
import com.projetjee.backend.repository.RessourceRepository;

@Service
public class ProjectRessourceService {

    private final ProjectRessourceRepository projectRessourceRepository;
    private final ProjectRepository projectRepository;
    private final RessourceRepository ressourceRepository;

    public ProjectRessourceService(ProjectRessourceRepository projectRessourceRepository,
            ProjectRepository projectRepository, RessourceRepository ressourceRepository) {
        this.projectRessourceRepository = projectRessourceRepository;
        this.projectRepository = projectRepository;
        this.ressourceRepository = ressourceRepository;
    }

    public List<ProjectRessourceResponse> recupererRessourcesProjet(Long projetId) {
        verifierProjetExiste(projetId);
        return projectRessourceRepository.findByProjetId(projetId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectRessourceResponse ajouterRessourceAuProjet(Long projetId, ProjectRessourceRequest request) {
        Project projet = verifierProjetExiste(projetId);
        Ressource ressource = recupererRessource(request.getRessourceId());

        if (projectRessourceRepository.existsByProjetIdAndRessourceId(projetId, request.getRessourceId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cette ressource est deja affectee au projet."
            );
        }

        ProjectRessource affectation = new ProjectRessource();
        affectation.setProjet(projet);
        affectation.setRessource(ressource);
        appliquerChamps(affectation, request, ressource);

        ProjectRessource affectationSauvegardee = projectRessourceRepository.save(affectation);
        ajusterBudgetProjet(projet, affectationSauvegardee.getCoutTotal());

        return toResponse(affectationSauvegardee);
    }

    @Transactional
    public ProjectRessourceResponse modifierRessourceProjet(Long projetId, Long affectationId,
            ProjectRessourceRequest request) {
        ProjectRessource affectation = recupererAffectation(projetId, affectationId);
        Ressource ressource = recupererRessource(request.getRessourceId());
        BigDecimal ancienCoutTotal = valeurOuZero(affectation.getCoutTotal());

        boolean changedRessource = !affectation.getRessource().getId().equals(request.getRessourceId());
        if (changedRessource
                && projectRessourceRepository.existsByProjetIdAndRessourceId(projetId, request.getRessourceId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cette ressource est deja affectee au projet."
            );
        }

        affectation.setRessource(ressource);
        appliquerChamps(affectation, request, ressource);

        ProjectRessource affectationSauvegardee = projectRessourceRepository.save(affectation);
        BigDecimal nouveauCoutTotal = valeurOuZero(affectationSauvegardee.getCoutTotal());
        ajusterBudgetProjet(affectationSauvegardee.getProjet(), nouveauCoutTotal.subtract(ancienCoutTotal));

        return toResponse(affectationSauvegardee);
    }

    @Transactional
    public void supprimerRessourceProjet(Long projetId, Long affectationId) {
        ProjectRessource affectation = recupererAffectation(projetId, affectationId);
        Project projet = affectation.getProjet();
        BigDecimal coutASoustraire = valeurOuZero(affectation.getCoutTotal()).negate();
        projectRessourceRepository.delete(affectation);
        ajusterBudgetProjet(projet, coutASoustraire);
    }

    public BigDecimal calculerCoutRessourcesProjet(Long projetId) {
        return projectRessourceRepository.findByProjetId(projetId).stream()
                .map(ProjectRessource::getCoutTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Project verifierProjetExiste(Long projetId) {
        return projectRepository.findById(projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + projetId
                ));
    }

    private Ressource recupererRessource(Long ressourceId) {
        return ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ressource introuvable avec l'id : " + ressourceId
                ));
    }

    private ProjectRessource recupererAffectation(Long projetId, Long affectationId) {
        verifierProjetExiste(projetId);
        return projectRessourceRepository.findByIdAndProjetId(affectationId, projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ressource de projet introuvable avec l'id : " + affectationId
                ));
    }

    private void appliquerChamps(ProjectRessource affectation, ProjectRessourceRequest request, Ressource ressource) {
        affectation.setQuantite(request.getQuantite());
        affectation.setCoutUnitaire(
                request.getCoutUnitaire() != null ? request.getCoutUnitaire() : ressource.getCout()
        );
        affectation.setNote(request.getNote());
    }

    private void ajusterBudgetProjet(Project projet, BigDecimal delta) {
        BigDecimal deltaBudget = valeurOuZero(delta);
        if (deltaBudget.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        projet.setBudget(valeurOuZero(projet.getBudget()).add(deltaBudget));
        projectRepository.save(projet);
    }

    private BigDecimal valeurOuZero(BigDecimal valeur) {
        return valeur != null ? valeur : BigDecimal.ZERO;
    }

    private ProjectRessourceResponse toResponse(ProjectRessource affectation) {
        Ressource ressource = affectation.getRessource();
        return new ProjectRessourceResponse(
                affectation.getId(),
                affectation.getProjet().getId(),
                ressource.getId(),
                ressource.getNom(),
                ressource.getType(),
                ressource.getDisponibilite(),
                affectation.getQuantite(),
                affectation.getCoutUnitaire(),
                affectation.getCoutTotal(),
                affectation.getNote()
        );
    }
}
