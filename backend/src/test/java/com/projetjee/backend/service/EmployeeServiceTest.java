package com.projetjee.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setNom("Doe");
        testEmployee.setEmail("john@example.com");
        testEmployee.setRole("ADMIN");
        testEmployee.setEquipe("IT");
    }

    @Test
    void testRecupererTousLesEmployes_Success() {
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.recupererTousLesEmployes();

        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getNom());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testRecupererEmployeParId_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        Employee result = employeeService.recupererEmployeParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testCreerEmploye_Success() {
        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.creerEmploye(testEmployee);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreerEmploye_DuplicateEmail() {
        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(Exception.class, () -> {
            employeeService.creerEmploye(testEmployee);
        });
    }

    @Test
    void testMettreAJourEmploye_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee updateData = new Employee();
        updateData.setNom("Smith");
        updateData.setEmail("john@example.com");
        updateData.setRole("USER");
        updateData.setEquipe("HR");

        Employee result = employeeService.mettreAJourEmploye(1L, updateData);

        assertNotNull(result);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testSupprimerEmploye_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        employeeService.supprimerEmploye(1L);

        verify(employeeRepository, times(1)).delete(testEmployee);
    }
}
