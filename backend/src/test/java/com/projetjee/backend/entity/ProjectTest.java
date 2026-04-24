package com.projetjee.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.projetjee.backend.enums.ProjectStatus;

@DisplayName("Project Entity Tests")
class ProjectTest {

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
    }

    @Test
    @DisplayName("Should create project with default constructor")
    void testDefaultConstructor() {
        assertNotNull(project);
        assertNotNull(project.getEmployes());
        assertTrue(project.getEmployes().isEmpty());
    }

    @Test
    @DisplayName("Should set and get id")
    void testId() {
        project.setId(1L);
        assertEquals(1L, project.getId());
    }

    @Test
    @DisplayName("Should set and get nom")
    void testNom() {
        project.setNom("Test Project");
        assertEquals("Test Project", project.getNom());
    }

    @Test
    @DisplayName("Should set and get dateDebut")
    void testDateDebut() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        project.setDateDebut(date);
        assertEquals(date, project.getDateDebut());
    }

    @Test
    @DisplayName("Should set and get dateFin")
    void testDateFin() {
        LocalDate date = LocalDate.of(2024, 12, 31);
        project.setDateFin(date);
        assertEquals(date, project.getDateFin());
    }

    @Test
    @DisplayName("Should set and get budget")
    void testBudget() {
        BigDecimal budget = BigDecimal.valueOf(10000.00);
        project.setBudget(budget);
        assertEquals(budget, project.getBudget());
    }

    @Test
    @DisplayName("Should set and get statut")
    void testStatut() {
        project.setStatut(ProjectStatus.En_Cours);
        assertEquals(ProjectStatus.En_Cours, project.getStatut());
    }

    @Test
    @DisplayName("Should handle employes list")
    void testEmployes() {
        List<Employee> employes = new ArrayList<>();
        Employee emp1 = new Employee("John", "john@example.com", "ADMIN", "IT");
        emp1.setId(1L);
        employes.add(emp1);

        project.setEmployes(employes);
        
        assertEquals(1, project.getEmployes().size());
        assertEquals("John", project.getEmployes().get(0).getNom());
    }

    @Test
    @DisplayName("Should handle null budget")
    void testNullBudget() {
        project.setBudget(null);
        assertNull(project.getBudget());
    }

    @Test
    @DisplayName("Should handle different project statuses")
    void testDifferentStatuses() {
        project.setStatut(ProjectStatus.Programmé);
        assertEquals(ProjectStatus.Programmé, project.getStatut());

        project.setStatut(ProjectStatus.En_Cours);
        assertEquals(ProjectStatus.En_Cours, project.getStatut());

        project.setStatut(ProjectStatus.Completé);
        assertEquals(ProjectStatus.Completé, project.getStatut());

        project.setStatut(ProjectStatus.Annulé);
        assertEquals(ProjectStatus.Annulé, project.getStatut());
    }

    @Test
    @DisplayName("Should update multiple properties")
    void testUpdateProperties() {
        project.setId(1L);
        project.setNom("Updated Project");
        project.setDateDebut(LocalDate.of(2024, 1, 1));
        project.setDateFin(LocalDate.of(2024, 12, 31));
        project.setBudget(BigDecimal.valueOf(50000.00));
        project.setStatut(ProjectStatus.En_Cours);

        assertEquals(1L, project.getId());
        assertEquals("Updated Project", project.getNom());
        assertEquals(LocalDate.of(2024, 1, 1), project.getDateDebut());
        assertEquals(LocalDate.of(2024, 12, 31), project.getDateFin());
        assertEquals(BigDecimal.valueOf(50000.00), project.getBudget());
        assertEquals(ProjectStatus.En_Cours, project.getStatut());
    }

    @Test
    @DisplayName("Should handle empty employes list")
    void testEmptyEmployes() {
        project.setEmployes(new ArrayList<>());
        assertTrue(project.getEmployes().isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple employees")
    void testMultipleEmployes() {
        List<Employee> employes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Employee emp = new Employee("Employee" + i, "emp" + i + "@example.com", "USER", "Team");
            emp.setId((long) i);
            employes.add(emp);
        }

        project.setEmployes(employes);
        
        assertEquals(5, project.getEmployes().size());
    }

    @Test
    @DisplayName("Should handle large budget")
    void testLargeBudget() {
        BigDecimal largeBudget = BigDecimal.valueOf(999999999.99);
        project.setBudget(largeBudget);
        assertEquals(largeBudget, project.getBudget());
    }
}
