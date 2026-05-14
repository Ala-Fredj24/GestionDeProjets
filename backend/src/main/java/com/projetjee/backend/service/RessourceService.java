package com.projetjee.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Ressource;
import com.projetjee.backend.repository.RessourceRepository;

@Service
public class RessourceService {

    private final RessourceRepository ressourceRepository;

    public RessourceService(RessourceRepository ressourceRepository) {
        this.ressourceRepository = ressourceRepository;
    }

    public List<Ressource> recupererToutesLesRessources() {
        return ressourceRepository.findAll();
    }

    public Ressource recupererRessourceParId(Long id) {
        return ressourceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ressource introuvable avec l'id : " + id
                ));
    }

    public Ressource creerRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    public Ressource mettreAJourRessource(Long id, Ressource ressourceDetails) {
        Ressource existingRessource = recupererRessourceParId(id);

        existingRessource.setNom(ressourceDetails.getNom());
        existingRessource.setType(ressourceDetails.getType());
        existingRessource.setCout(ressourceDetails.getCout());
        existingRessource.setDisponibilite(ressourceDetails.getDisponibilite());

        return ressourceRepository.save(existingRessource);
    }

    public void supprimerRessource(Long id) {
        Ressource existingRessource = recupererRessourceParId(id);
        ressourceRepository.delete(existingRessource);
    }
}
