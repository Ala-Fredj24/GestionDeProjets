package com.projetjee.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Ressource;
import com.projetjee.backend.repository.RessourceRepository;

@ExtendWith(MockitoExtension.class)
class RessourceServiceTest {

    @Mock
    private RessourceRepository ressourceRepository;

    @InjectMocks
    private RessourceService ressourceService;

    private Ressource testRessource;

    @BeforeEach
    void setUp() {
        testRessource = new Ressource();
        testRessource.setId(1L);
        testRessource.setNom("Serveur cloud");
        testRessource.setType("Materielle");
        testRessource.setCout(new BigDecimal("500.00"));
        testRessource.setDisponibilite(true);
    }

    @Test
    void testRecupererToutesLesRessources_Success() {
        when(ressourceRepository.findAll()).thenReturn(List.of(testRessource));

        List<Ressource> result = ressourceService.recupererToutesLesRessources();

        assertEquals(1, result.size());
        assertEquals("Serveur cloud", result.get(0).getNom());
        verify(ressourceRepository, times(1)).findAll();
    }

    @Test
    void testRecupererRessourceParId_Success() {
        when(ressourceRepository.findById(1L)).thenReturn(Optional.of(testRessource));

        Ressource result = ressourceService.recupererRessourceParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ressourceRepository, times(1)).findById(1L);
    }

    @Test
    void testRecupererRessourceParId_NotFound() {
        when(ressourceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ressourceService.recupererRessourceParId(99L));
    }

    @Test
    void testCreerRessource_Success() {
        when(ressourceRepository.save(any(Ressource.class))).thenReturn(testRessource);

        Ressource result = ressourceService.creerRessource(testRessource);

        assertNotNull(result);
        assertEquals("Serveur cloud", result.getNom());
        verify(ressourceRepository, times(1)).save(testRessource);
    }

    @Test
    void testMettreAJourRessource_Success() {
        Ressource updateData = new Ressource("Budget licence", "Financiere", new BigDecimal("1200.00"), false);
        when(ressourceRepository.findById(1L)).thenReturn(Optional.of(testRessource));
        when(ressourceRepository.save(any(Ressource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ressource result = ressourceService.mettreAJourRessource(1L, updateData);

        assertEquals("Budget licence", result.getNom());
        assertEquals("Financiere", result.getType());
        assertEquals(new BigDecimal("1200.00"), result.getCout());
        assertFalse(result.getDisponibilite());
        verify(ressourceRepository, times(1)).save(testRessource);
    }

    @Test
    void testSupprimerRessource_Success() {
        when(ressourceRepository.findById(1L)).thenReturn(Optional.of(testRessource));

        ressourceService.supprimerRessource(1L);

        verify(ressourceRepository, times(1)).delete(testRessource);
    }
}
