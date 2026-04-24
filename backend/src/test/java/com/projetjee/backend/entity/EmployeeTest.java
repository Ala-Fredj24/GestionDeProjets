package com.projetjee.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Employee Entity Tests")
class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
    }

    @Test
    @DisplayName("Should create employee with default constructor")
    void testDefaultConstructor() {
        assertNotNull(employee);
    }

    @Test
    @DisplayName("Should create employee with full constructor")
    void testFullConstructor() {
        Employee emp = new Employee("John Doe", "john@example.com", "ADMIN", "IT");
        
        assertEquals("John Doe", emp.getNom());
        assertEquals("john@example.com", emp.getEmail());
        assertEquals("ADMIN", emp.getRole());
        assertEquals("IT", emp.getEquipe());
    }

    @Test
    @DisplayName("Should set and get id")
    void testId() {
        employee.setId(1L);
        assertEquals(1L, employee.getId());
    }

    @Test
    @DisplayName("Should set and get nom")
    void testNom() {
        employee.setNom("Jane Doe");
        assertEquals("Jane Doe", employee.getNom());
    }

    @Test
    @DisplayName("Should set and get email")
    void testEmail() {
        employee.setEmail("jane@example.com");
        assertEquals("jane@example.com", employee.getEmail());
    }

    @Test
    @DisplayName("Should set and get role")
    void testRole() {
        employee.setRole("ADMIN");
        assertEquals("ADMIN", employee.getRole());
    }

    @Test
    @DisplayName("Should set and get equipe")
    void testEquipe() {
        employee.setEquipe("Finance");
        assertEquals("Finance", employee.getEquipe());
    }

    @Test
    @DisplayName("Should handle null values")
    void testNullValues() {
        employee.setNom(null);
        employee.setEmail(null);
        employee.setRole(null);
        employee.setEquipe(null);

        assertNull(employee.getNom());
        assertNull(employee.getEmail());
        assertNull(employee.getRole());
        assertNull(employee.getEquipe());
    }

    @Test
    @DisplayName("Should update multiple properties")
    void testUpdateProperties() {
        employee.setId(1L);
        employee.setNom("John");
        employee.setEmail("john@example.com");
        employee.setRole("USER");
        employee.setEquipe("HR");

        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getNom());
        assertEquals("john@example.com", employee.getEmail());
        assertEquals("USER", employee.getRole());
        assertEquals("HR", employee.getEquipe());

        employee.setNom("Jane");
        employee.setRole("ADMIN");
        
        assertEquals("Jane", employee.getNom());
        assertEquals("ADMIN", employee.getRole());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        employee.setNom("");
        employee.setEmail("");
        employee.setRole("");
        employee.setEquipe("");

        assertEquals("", employee.getNom());
        assertEquals("", employee.getEmail());
        assertEquals("", employee.getRole());
        assertEquals("", employee.getEquipe());
    }
}
