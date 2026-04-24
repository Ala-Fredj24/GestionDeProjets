package com.projetjee.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.enums.TaskPriority;
import com.projetjee.backend.enums.TaskStatus;

@DisplayName("Task Entity Tests")
class TaskTest {

    private Task task;
    private Project testProject;

    @BeforeEach
    void setUp() {
        task = new Task();
        testProject = new Project();
        testProject.setId(1L);
        testProject.setStatut(ProjectStatus.En_Cours);
    }

    @Test
    @DisplayName("Should create task with default constructor")
    void testDefaultConstructor() {
        assertNotNull(task);
    }

    @Test
    @DisplayName("Should set and get id")
    void testId() {
        task.setId(1L);
        assertEquals(1L, task.getId());
    }

    @Test
    @DisplayName("Should set and get projet")
    void testProjet() {
        task.setProjet(testProject);
        assertEquals(testProject, task.getProjet());
    }

    @Test
    @DisplayName("Should set and get responsable")
    void testResponsable() {
        task.setResponsable("John Doe");
        assertEquals("John Doe", task.getResponsable());
    }

    @Test
    @DisplayName("Should set and get description")
    void testDescription() {
        task.setDescription("Task Description");
        assertEquals("Task Description", task.getDescription());
    }

    @Test
    @DisplayName("Should set and get statut")
    void testStatut() {
        task.setStatut(TaskStatus.En_Cours);
        assertEquals(TaskStatus.En_Cours, task.getStatut());
    }

    @Test
    @DisplayName("Should set and get priorite")
    void testPriorite() {
        task.setPriorite(TaskPriority.Moyenne);
        assertEquals(TaskPriority.Moyenne, task.getPriorite());
    }

    @Test
    @DisplayName("Should set and get dateLimite")
    void testDateLimite() {
        LocalDate date = LocalDate.of(2024, 12, 31);
        task.setDateLimite(date);
        assertEquals(date, task.getDateLimite());
    }

    @Test
    @DisplayName("Should set and get coutPrevu")
    void testCoutPrevu() {
        BigDecimal cout = BigDecimal.valueOf(1000.00);
        task.setCoutPrevu(cout);
        assertEquals(cout, task.getCoutPrevu());
    }

    @Test
    @DisplayName("Should set and get coutReel")
    void testCoutReel() {
        BigDecimal cout = BigDecimal.valueOf(950.00);
        task.setCoutReel(cout);
        assertEquals(cout, task.getCoutReel());
    }

    @Test
    @DisplayName("Should handle all task statuses")
    void testAllStatuses() {
        task.setStatut(TaskStatus.À_Faire);
        assertEquals(TaskStatus.À_Faire, task.getStatut());

        task.setStatut(TaskStatus.En_Cours);
        assertEquals(TaskStatus.En_Cours, task.getStatut());

        task.setStatut(TaskStatus.Complété);
        assertEquals(TaskStatus.Complété, task.getStatut());

        task.setStatut(TaskStatus.Annulé);
        assertEquals(TaskStatus.Annulé, task.getStatut());
    }

    @Test
    @DisplayName("Should handle all task priorities")
    void testAllPriorities() {
        task.setPriorite(TaskPriority.Faible);
        assertEquals(TaskPriority.Faible, task.getPriorite());

        task.setPriorite(TaskPriority.Moyenne);
        assertEquals(TaskPriority.Moyenne, task.getPriorite());

        task.setPriorite(TaskPriority.Elevé);
        assertEquals(TaskPriority.Elevé, task.getPriorite());
    }

    @Test
    @DisplayName("Should update multiple properties")
    void testUpdateProperties() {
        task.setId(1L);
        task.setProjet(testProject);
        task.setResponsable("Jane Doe");
        task.setDescription("Updated Description");
        task.setStatut(TaskStatus.En_Cours);
        task.setPriorite(TaskPriority.Elevé);
        task.setDateLimite(LocalDate.of(2024, 6, 30));

        assertEquals(1L, task.getId());
        assertEquals(testProject, task.getProjet());
        assertEquals("Jane Doe", task.getResponsable());
        assertEquals("Updated Description", task.getDescription());
        assertEquals(TaskStatus.En_Cours, task.getStatut());
        assertEquals(TaskPriority.Elevé, task.getPriorite());
        assertEquals(LocalDate.of(2024, 6, 30), task.getDateLimite());
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        task.setProjet(null);
        task.setCoutPrevu(null);
        task.setCoutReel(null);

        assertNull(task.getProjet());
        assertNull(task.getCoutPrevu());
        assertNull(task.getCoutReel());
    }

    @Test
    @DisplayName("Should handle zero costs")
    void testZeroCosts() {
        task.setCoutPrevu(BigDecimal.ZERO);
        task.setCoutReel(BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, task.getCoutPrevu());
        assertEquals(BigDecimal.ZERO, task.getCoutReel());
    }

    @Test
    @DisplayName("Should handle large costs")
    void testLargeCosts() {
        BigDecimal largeCost = BigDecimal.valueOf(999999.99);
        task.setCoutPrevu(largeCost);
        task.setCoutReel(largeCost);

        assertEquals(largeCost, task.getCoutPrevu());
        assertEquals(largeCost, task.getCoutReel());
    }
}
