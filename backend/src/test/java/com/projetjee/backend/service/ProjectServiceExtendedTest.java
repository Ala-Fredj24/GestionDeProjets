package com.projetjee.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.repository.EmployeeRepository;
import com.projetjee.backend.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService Tests")
class ProjectServiceExtendedTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setNom("Test Project");
        testProject.setDateDebut(LocalDate.of(2024, 1, 1));
        testProject.setDateFin(LocalDate.of(2024, 12, 31));
        testProject.setBudget(BigDecimal.valueOf(10000.0));
        testProject.setStatut(ProjectStatus.En_Cours);

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setNom("Doe");
        testEmployee.setEmail("john@example.com");
    }

    // ===== recupererTousLesProjets =====
    @Test
    @DisplayName("Should retrieve all projects successfully")
    void testRecupererTousLesProjets_Success() {
        List<Project> projects = List.of(testProject);
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.recupererTousLesProjets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getNom());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no projects")
    void testRecupererTousLesProjets_EmptyList() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<Project> result = projectService.recupererTousLesProjets();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectRepository, times(1)).findAll();
    }

    // ===== recupererProjetParId =====
    @Test
    @DisplayName("Should retrieve project by ID successfully")
    void testRecupererProjetParId_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        Project result = projectService.recupererProjetParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Project", result.getNom());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found")
    void testRecupererProjetParId_NotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            projectService.recupererProjetParId(99L);
        });
        verify(projectRepository, times(1)).findById(99L);
    }

    // ===== creerProjet =====
    @Test
    @DisplayName("Should create project with valid dates")
    void testCreerProjet_Success() {
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        Project result = projectService.creerProjet(testProject);

        assertNotNull(result);
        assertEquals("Test Project", result.getNom());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw exception when end date before start date")
    void testCreerProjet_InvalidDates() {
        Project invalidProject = new Project();
        invalidProject.setNom("Invalid");
        invalidProject.setDateDebut(LocalDate.of(2024, 12, 31));
        invalidProject.setDateFin(LocalDate.of(2024, 1, 1));

        assertThrows(ResponseStatusException.class, () -> {
            projectService.creerProjet(invalidProject);
        });
    }

    @Test
    @DisplayName("Should create project with null dates")
    void testCreerProjet_NullDates() {
        Project projectWithNullDates = new Project();
        projectWithNullDates.setNom("Project");
        projectWithNullDates.setDateDebut(null);
        projectWithNullDates.setDateFin(null);

        when(projectRepository.save(any(Project.class))).thenReturn(projectWithNullDates);

        Project result = projectService.creerProjet(projectWithNullDates);

        assertNotNull(result);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    // ===== mettreAJourProjet =====
    @Test
    @DisplayName("Should update project successfully")
    void testMettreAJourProjet_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        Project updateData = new Project();
        updateData.setNom("Updated");
        updateData.setDateDebut(LocalDate.of(2024, 2, 1));
        updateData.setDateFin(LocalDate.of(2024, 11, 30));
        updateData.setBudget(BigDecimal.valueOf(15000.0));
        updateData.setStatut(ProjectStatus.Completé);

        Project result = projectService.mettreAJourProjet(1L, updateData);

        assertNotNull(result);
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent project")
    void testMettreAJourProjet_NotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            projectService.mettreAJourProjet(99L, testProject);
        });
    }

    // ===== supprimerProjet =====
    @Test
    @DisplayName("Should delete project successfully")
    void testSupprimerProjet_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        doNothing().when(projectRepository).delete(testProject);

        projectService.supprimerProjet(1L);

        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).delete(testProject);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent project")
    void testSupprimerProjet_NotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            projectService.supprimerProjet(99L);
        });
    }

    // ===== affecterEmployesAuProjet =====
    @Test
    @DisplayName("Should assign employees to project successfully")
    void testAffecterEmployesAuProjet_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(employeeRepository.findAllById(List.of(1L))).thenReturn(List.of(testEmployee));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        Project result = projectService.affecterEmployesAuProjet(1L, List.of(1L));

        assertNotNull(result);
        verify(projectRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findAllById(List.of(1L));
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Should assign empty list to project")
    void testAffecterEmployesAuProjet_EmptyList() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        Project result = projectService.affecterEmployesAuProjet(1L, List.of());

        assertNotNull(result);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw exception when employee not found")
    void testAffecterEmployesAuProjet_EmployeeNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(employeeRepository.findAllById(List.of(99L))).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> {
            projectService.affecterEmployesAuProjet(1L, List.of(99L));
        });
    }

    @Test
    @DisplayName("Should throw exception when partial employee list")
    void testAffecterEmployesAuProjet_PartialEmployeeList() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(employeeRepository.findAllById(List.of(1L, 99L))).thenReturn(List.of(testEmployee));

        assertThrows(ResponseStatusException.class, () -> {
            projectService.affecterEmployesAuProjet(1L, List.of(1L, 99L));
        });
    }

    @Test
    @DisplayName("Should throw exception when project not found for assignment")
    void testAffecterEmployesAuProjet_ProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            projectService.affecterEmployesAuProjet(99L, List.of(1L));
        });
    }
}
