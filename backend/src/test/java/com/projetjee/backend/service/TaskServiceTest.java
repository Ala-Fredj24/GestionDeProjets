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

import com.projetjee.backend.entity.Task;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.enums.TaskStatus;
import com.projetjee.backend.enums.TaskPriority;
import com.projetjee.backend.repository.TaskRepository;
import com.projetjee.backend.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setProjet(testProject);
        testTask.setResponsable("John Doe");
        testTask.setDescription("Test Description");
        testTask.setStatut(TaskStatus.À_Faire);
        testTask.setPriorite(TaskPriority.Moyenne);
        testTask.setDateLimite(LocalDate.of(2024, 12, 31));
        testTask.setCoutReel(BigDecimal.ZERO);
        testTask.setCoutPrevu(BigDecimal.ZERO);
    }

    @Test
    void testRecupererToutesLesTaches_Success() {
        List<Task> tasks = List.of(testTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.recupererToutesLesTaches();

        assertEquals(1, result.size());
        assertEquals("Test Description", result.get(0).getDescription());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testRecupererTacheParId_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Task result = taskService.recupererTacheParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testCreerTache_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.creerTache(testTask);

        assertNotNull(result);
        assertEquals("Test Description", result.getDescription());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testMettreAJourTache_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task updateData = new Task();
        updateData.setProjet(testProject);
        updateData.setResponsable("Jane Doe");
        updateData.setDescription("Updated Description");
        updateData.setStatut(TaskStatus.En_Cours);
        updateData.setDateLimite(LocalDate.of(2024, 11, 30));
        updateData.setPriorite(TaskPriority.Elevé);

        Task result = taskService.mettreAJourTache(1L, updateData);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testSupprimerTache_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        taskService.supprimerTache(1L);

        verify(taskRepository, times(1)).delete(testTask);
    }
}
