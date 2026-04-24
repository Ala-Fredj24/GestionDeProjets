package com.projetjee.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RapportFinancierProjetDto Tests")
class RapportFinancierProjetDtoTest {

    private RapportFinancierProjetDto dto;

    @BeforeEach
    void setUp() {
        dto = new RapportFinancierProjetDto();
    }

    @Test
    @DisplayName("Should create DTO with default constructor")
    void testDefaultConstructor() {
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Should set and get projetId")
    void testProjetId() {
        dto.setProjetId(1L);
        assertEquals(1L, dto.getProjetId());
    }

    @Test
    @DisplayName("Should set and get nomProjet")
    void testNomProjet() {
        dto.setNomProjet("Test Project");
        assertEquals("Test Project", dto.getNomProjet());
    }

    @Test
    @DisplayName("Should set and get statutProjet")
    void testStatutProjet() {
        dto.setStatutProjet("En_Cours");
        assertEquals("En_Cours", dto.getStatutProjet());
    }

    @Test
    @DisplayName("Should set and get budgetProjet")
    void testBudgetProjet() {
        BigDecimal budget = BigDecimal.valueOf(10000.0);
        dto.setBudgetProjet(budget);
        assertEquals(budget, dto.getBudgetProjet());
    }

    @Test
    @DisplayName("Should set and get coutPrevuTotal")
    void testCoutPrevuTotal() {
        BigDecimal cout = BigDecimal.valueOf(5000.0);
        dto.setCoutPrevuTotal(cout);
        assertEquals(cout, dto.getCoutPrevuTotal());
    }

    @Test
    @DisplayName("Should set and get coutReelTotal")
    void testCoutReelTotal() {
        BigDecimal cout = BigDecimal.valueOf(4500.0);
        dto.setCoutReelTotal(cout);
        assertEquals(cout, dto.getCoutReelTotal());
    }

    @Test
    @DisplayName("Should set and get ecartPrevuReel")
    void testEcartPrevuReel() {
        BigDecimal ecart = BigDecimal.valueOf(500.0);
        dto.setEcartPrevuReel(ecart);
        assertEquals(ecart, dto.getEcartPrevuReel());
    }

    @Test
    @DisplayName("Should set and get resteBudget")
    void testResteBudget() {
        BigDecimal reste = BigDecimal.valueOf(5500.0);
        dto.setResteBudget(reste);
        assertEquals(reste, dto.getResteBudget());
    }

    @Test
    @DisplayName("Should set and get tauxConsommation")
    void testTauxConsommation() {
        BigDecimal taux = BigDecimal.valueOf(45.0);
        dto.setTauxConsommation(taux);
        assertEquals(taux, dto.getTauxConsommation());
    }

    @Test
    @DisplayName("Should set and get depasseBudget")
    void testDepasseBudget() {
        dto.setDepasseBudget(true);
        assertTrue(dto.isDepasseBudget());

        dto.setDepasseBudget(false);
        assertFalse(dto.isDepasseBudget());
    }

    @Test
    @DisplayName("Should set and get nombreTaches")
    void testNombreTaches() {
        dto.setNombreTaches(5);
        assertEquals(5, dto.getNombreTaches());
    }

    @Test
    @DisplayName("Should work with full constructor")
    void testFullConstructor() {
        BigDecimal budget = BigDecimal.valueOf(10000.0);
        BigDecimal coutPrevu = BigDecimal.valueOf(5000.0);
        BigDecimal coutReel = BigDecimal.valueOf(4500.0);
        BigDecimal ecart = BigDecimal.valueOf(500.0);
        BigDecimal reste = BigDecimal.valueOf(5500.0);
        BigDecimal taux = BigDecimal.valueOf(45.0);

        RapportFinancierProjetDto dtoFull = new RapportFinancierProjetDto(
                1L, "Test Project", "En_Cours", budget, coutPrevu, coutReel, ecart, reste, taux, false, 5);

        assertEquals(1L, dtoFull.getProjetId());
        assertEquals("Test Project", dtoFull.getNomProjet());
        assertEquals("En_Cours", dtoFull.getStatutProjet());
        assertEquals(budget, dtoFull.getBudgetProjet());
        assertEquals(coutPrevu, dtoFull.getCoutPrevuTotal());
        assertEquals(coutReel, dtoFull.getCoutReelTotal());
        assertEquals(ecart, dtoFull.getEcartPrevuReel());
        assertEquals(reste, dtoFull.getResteBudget());
        assertEquals(taux, dtoFull.getTauxConsommation());
        assertFalse(dtoFull.isDepasseBudget());
        assertEquals(5, dtoFull.getNombreTaches());
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        dto.setNomProjet(null);
        assertNull(dto.getNomProjet());

        dto.setBudgetProjet(null);
        assertNull(dto.getBudgetProjet());
    }

    @Test
    @DisplayName("Should handle BigDecimal with various scales")
    void testBigDecimalVariations() {
        BigDecimal bd1 = BigDecimal.valueOf(1000.50);
        BigDecimal bd2 = BigDecimal.valueOf(2000);
        BigDecimal bd3 = new BigDecimal("3000.123456");

        dto.setBudgetProjet(bd1);
        dto.setCoutPrevuTotal(bd2);
        dto.setCoutReelTotal(bd3);

        assertEquals(bd1, dto.getBudgetProjet());
        assertEquals(bd2, dto.getCoutPrevuTotal());
        assertEquals(bd3, dto.getCoutReelTotal());
    }

    @Test
    @DisplayName("Should support multiple instances independently")
    void testMultipleInstances() {
        RapportFinancierProjetDto dto1 = new RapportFinancierProjetDto();
        RapportFinancierProjetDto dto2 = new RapportFinancierProjetDto();

        dto1.setProjetId(1L);
        dto2.setProjetId(2L);

        assertEquals(1L, dto1.getProjetId());
        assertEquals(2L, dto2.getProjetId());
    }
}
