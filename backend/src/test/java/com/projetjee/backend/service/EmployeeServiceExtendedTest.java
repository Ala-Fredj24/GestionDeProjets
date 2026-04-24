package com.projetjee.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Extended Tests")
class EmployeeServiceExtendedTest {

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

    // ===== recupererTousLesEmployes =====
    @Test
    @DisplayName("Should retrieve all employees successfully")
    void testRecupererTousLesEmployes_Success() {
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.recupererTousLesEmployes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getNom());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no employees")
    void testRecupererTousLesEmployes_EmptyList() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = employeeService.recupererTousLesEmployes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    // ===== recupererEmployeParId =====
    @Test
    @DisplayName("Should retrieve employee by ID successfully")
    void testRecupererEmployeParId_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        Employee result = employeeService.recupererEmployeParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Doe", result.getNom());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when employee not found")
    void testRecupererEmployeParId_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.recupererEmployeParId(99L);
        });
        verify(employeeRepository, times(1)).findById(99L);
    }

    // ===== creerEmploye =====
    @Test
    @DisplayName("Should create employee with unique email")
    void testCreerEmploye_Success() {
        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.creerEmploye(testEmployee);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        verify(employeeRepository, times(1)).existsByEmail("john@example.com");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreerEmploye_DuplicateEmail() {
        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.creerEmploye(testEmployee);
        });
        verify(employeeRepository, times(1)).existsByEmail("john@example.com");
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ===== mettreAJourEmploye =====
    @Test
    @DisplayName("Should update employee with same email")
    void testMettreAJourEmploye_SameEmail() {
        Employee updateData = new Employee();
        updateData.setNom("Smith");
        updateData.setEmail("john@example.com");
        updateData.setRole("USER");
        updateData.setEquipe("HR");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.mettreAJourEmploye(1L, updateData);

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should update employee with new unique email")
    void testMettreAJourEmploye_NewEmail() {
        Employee updateData = new Employee();
        updateData.setNom("Smith");
        updateData.setEmail("jane@example.com");
        updateData.setRole("USER");
        updateData.setEquipe("HR");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.mettreAJourEmploye(1L, updateData);

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).existsByEmail("jane@example.com");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw exception when new email already exists")
    void testMettreAJourEmploye_DuplicateEmail() {
        Employee updateData = new Employee();
        updateData.setNom("Smith");
        updateData.setEmail("duplicate@example.com");
        updateData.setRole("USER");
        updateData.setEquipe("HR");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.mettreAJourEmploye(1L, updateData);
        });
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent employee")
    void testMettreAJourEmploye_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.mettreAJourEmploye(99L, testEmployee);
        });
    }

    // ===== supprimerEmploye =====
    @Test
    @DisplayName("Should delete employee successfully")
    void testSupprimerEmploye_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        doNothing().when(employeeRepository).delete(testEmployee);

        employeeService.supprimerEmploye(1L);

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).delete(testEmployee);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent employee")
    void testSupprimerEmploye_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            employeeService.supprimerEmploye(99L);
        });
    }
}
