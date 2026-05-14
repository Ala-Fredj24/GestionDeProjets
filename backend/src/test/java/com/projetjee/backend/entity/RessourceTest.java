package com.projetjee.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Ressource Entity Tests")
class RessourceTest {

    private Ressource ressource;

    @BeforeEach
    void setUp() {
        ressource = new Ressource();
    }

    @Test
    @DisplayName("Should create ressource with default constructor")
    void testDefaultConstructor() {
        assertNotNull(ressource);
        assertEquals(BigDecimal.ZERO, ressource.getCout());
        assertEquals(Boolean.TRUE, ressource.getDisponibilite());
    }

    @Test
    @DisplayName("Should create ressource with full constructor")
    void testFullConstructor() {
        Ressource materiel = new Ressource("Serveur cloud", "Materielle", new BigDecimal("500.00"), true);

        assertEquals("Serveur cloud", materiel.getNom());
        assertEquals("Materielle", materiel.getType());
        assertEquals(new BigDecimal("500.00"), materiel.getCout());
        assertTrue(materiel.getDisponibilite());
    }

    @Test
    @DisplayName("Should set and get all properties")
    void testSettersAndGetters() {
        ressource.setId(1L);
        ressource.setNom("Budget licence");
        ressource.setType("Financiere");
        ressource.setCout(new BigDecimal("1200.00"));
        ressource.setDisponibilite(false);

        assertEquals(1L, ressource.getId());
        assertEquals("Budget licence", ressource.getNom());
        assertEquals("Financiere", ressource.getType());
        assertEquals(new BigDecimal("1200.00"), ressource.getCout());
        assertFalse(ressource.getDisponibilite());
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        ressource.setNom(null);
        ressource.setType(null);
        ressource.setCout(null);
        ressource.setDisponibilite(null);

        assertNull(ressource.getNom());
        assertNull(ressource.getType());
        assertNull(ressource.getCout());
        assertNull(ressource.getDisponibilite());
    }
}
