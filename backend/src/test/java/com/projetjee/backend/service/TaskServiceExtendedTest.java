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

import com.projetjee.backend.entity.Task;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.enums.TaskStatus;
import com.projetjee.backend.enums.TaskPriority;
import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.repository.TaskRepository;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Extended Tests")
class TaskServiceExtendedTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setStatut(ProjectStatus.En_Cours);

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

    // ===== recupererToutesLesTaches =====
    @Test
    @DisplayName("Should retrieve all tasks successfully")
    void testRecupererToutesLesTaches_Success() {
        List<Task> tasks = List.of(testTask);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.recupererToutesLesTaches();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no tasks")
    void testRecupererToutesLesTaches_EmptyList() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<Task> result = taskService.recupererToutesLesTaches();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    // ===== recupererTachesParProjetId =====
    @Test
    @DisplayName("Should retrieve tasks by project ID")
    void testRecupererTachesParProjetId_Success() {
        List<Task> tasks = List.of(testTask);
        when(taskRepository.findByProjetId(1L)).thenReturn(tasks);

        List<Task> result = taskService.recupererTachesParProjetId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findByProjetId(1L);
    }

    @Test
    @DisplayName("Should return empty list when project has no tasks")
    void testRecupererTachesParProjetId_EmptyList() {
        when(taskRepository.findByProjetId(1L)).thenReturn(Collections.emptyList());

        List<Task> result = taskService.recupererTachesParProjetId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findByProjetId(1L);
    }

    // ===== recupererTacheParId =====
    @Test
    @DisplayName("Should retrieve task by ID successfully")
    void testRecupererTacheParId_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        Task result = taskService.recupererTacheParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void testRecupererTacheParId_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.recupererTacheParId(99L);
        });
        verify(taskRepository, times(1)).findById(99L);
    }

    // ===== creerTache =====
    @Test
    @DisplayName("Should create task successfully")
    void testCreerTache_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.creerTache(testTask);

        assertNotNull(result);
        assertEquals("Test Description", result.getDescription());
        verify(projectRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when project not found for task creation")
    void testCreerTache_ProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        testTask.getProjet().setId(99L);

        assertThrows(ResponseStatusException.class, () -> {
            taskService.creerTache(testTask);
        });
    }

    @Test
    @DisplayName("Should initialize null costs to zero")
    void testCreerTache_NullCosts() {
        Task taskWithNullCosts = new Task();
        taskWithNullCosts.setProjet(testProject);
        taskWithNullCosts.setResponsable("John");
        taskWithNullCosts.setDescription("Desc");
        taskWithNullCosts.setStatut(TaskStatus.À_Faire);
        taskWithNullCosts.setPriorite(TaskPriority.Moyenne);
        taskWithNullCosts.setDateLimite(LocalDate.of(2024, 12, 31));
        taskWithNullCosts.setCoutReel(null);
        taskWithNullCosts.setCoutPrevu(null);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(taskWithNullCosts);

        Task result = taskService.creerTache(taskWithNullCosts);

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    // ===== mettreAJourTache =====
    @Test
    @DisplayName("Should update task successfully")
    void testMettreAJourTache_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task updateData = new Task();
        updateData.setProjet(testProject);
        updateData.setResponsable("Jane Doe");
        updateData.setDescription("Updated");
        updateData.setStatut(TaskStatus.En_Cours);
        updateData.setDateLimite(LocalDate.of(2024, 11, 30));
        updateData.setPriorite(TaskPriority.Elevé);

        Task result = taskService.mettreAJourTache(1L, updateData);

        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    void testMettreAJourTache_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.mettreAJourTache(99L, testTask);
        });
    }

    @Test
    @DisplayName("Should throw exception when project not found during update")
    void testMettreAJourTache_ProjectNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.mettreAJourTache(1L, testTask);
        });
    }

    // ===== supprimerTache =====
    @Test
    @DisplayName("Should delete task successfully")
    void testSupprimerTache_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        doNothing().when(taskRepository).delete(testTask);

        taskService.supprimerTache(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(testTask);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent task")
    void testSupprimerTache_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.supprimerTache(99L);
        });
    }
}
