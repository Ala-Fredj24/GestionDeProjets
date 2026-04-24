package com.projetjee.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projetjee.backend.entity.Project;
import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setNom("Test Project");
        testProject.setDateDebut(LocalDate.of(2024, 1, 1));
        testProject.setDateFin(LocalDate.of(2024, 12, 31));
        testProject.setBudget(BigDecimal.valueOf(10000.0));
        testProject.setStatut(ProjectStatus.En_Cours);
    }

    @Test
    void testRecupererTousLesProjets_Success() {
        List<Project> projects = List.of(testProject);
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.recupererTousLesProjets();

        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getNom());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testRecupererProjetParId_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        Project result = projectService.recupererProjetParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Project", result.getNom());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void testCreerProjet_Success() {
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        Project result = projectService.creerProjet(testProject);

        assertNotNull(result);
        assertEquals("Test Project", result.getNom());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testMettreAJourProjet_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        Project updateData = new Project();
        updateData.setNom("Updated Project");
        updateData.setDateDebut(LocalDate.of(2024, 2, 1));
        updateData.setDateFin(LocalDate.of(2024, 11, 30));
        updateData.setBudget(BigDecimal.valueOf(15000.0));
        updateData.setStatut(ProjectStatus.Completé);

        Project result = projectService.mettreAJourProjet(1L, updateData);

        assertNotNull(result);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testSupprimerProjet_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        projectService.supprimerProjet(1L);

        verify(projectRepository, times(1)).delete(testProject);
    }
}
