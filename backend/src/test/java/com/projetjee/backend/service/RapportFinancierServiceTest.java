package com.projetjee.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

import com.projetjee.backend.dto.RapportFinancierProjetDto;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("RapportFinancierService Tests")
class RapportFinancierServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRessourceService projectRessourceService;

    @InjectMocks
    private RapportFinancierService rapportFinancierService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setNom("Test Project");
        testProject.setBudget(BigDecimal.valueOf(10000.0));
        testProject.setStatut(ProjectStatus.En_Cours);
    }

    @Test
    @DisplayName("Should load all financial reports")
    void testChargerTousLesRapportsFinanciers_Success() {
        List<Project> projects = List.of(testProject);
        when(projectRepository.findAll()).thenReturn(projects);
        when(taskRepository.findByProjetId(1L)).thenReturn(Collections.emptyList());
        when(projectRessourceService.calculerCoutRessourcesProjet(1L)).thenReturn(BigDecimal.ZERO);

        List<RapportFinancierProjetDto> result = rapportFinancierService.chargerTousLesRapportsFinanciers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no projects")
    void testChargerTousLesRapportsFinanciers_EmptyList() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<RapportFinancierProjetDto> result = rapportFinancierService.chargerTousLesRapportsFinanciers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should load financial report by project ID")
    void testChargerRapportFinancierParProjet_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.findByProjetId(1L)).thenReturn(Collections.emptyList());
        when(projectRessourceService.calculerCoutRessourcesProjet(1L)).thenReturn(BigDecimal.ZERO);

        RapportFinancierProjetDto result = rapportFinancierService.chargerRapportFinancierParProjet(1L);

        assertNotNull(result);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found for financial report")
    void testChargerRapportFinancierParProjet_NotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            rapportFinancierService.chargerRapportFinancierParProjet(99L);
        });
        verify(projectRepository, times(1)).findById(99L);
    }
}
