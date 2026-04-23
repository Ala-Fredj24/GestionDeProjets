package com.projetjee.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetjee.backend.dto.RapportFinancierProjetDto;
import com.projetjee.backend.service.RapportFinancierService;

@RestController
@RequestMapping("/api/rapports-financiers")
@CrossOrigin(origins = "http://localhost:4200")
public class RapportFinancierController {

    private final RapportFinancierService rapportFinancierService;

    public RapportFinancierController(RapportFinancierService rapportFinancierService) {
        this.rapportFinancierService = rapportFinancierService;
    }

    @GetMapping("/projets")
    public List<RapportFinancierProjetDto> chargerTousLesRapportsFinanciers() {
        return rapportFinancierService.chargerTousLesRapportsFinanciers();
    }

    @GetMapping("/projets/{projetId}")
    public RapportFinancierProjetDto chargerRapportFinancierParProjet(@PathVariable Long projetId) {
        return rapportFinancierService.chargerRapportFinancierParProjet(projetId);
    }
}