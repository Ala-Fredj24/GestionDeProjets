package com.projetjee.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Task;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.TaskRepository;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public TaskService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public List<Task> recupererToutesLesTaches() {
        return taskRepository.findAll();
    }

    public List<Task> recupererTachesParProjetId(Long projetId) {
        return taskRepository.findByProjetId(projetId);
    }

    public Task recupererTacheParId(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tâche introuvable avec l'id : " + id
                ));
    }

    public Task creerTache(Task task) {
        Long projetId = task.getProjet().getId();

        Project existingProject = projectRepository.findById(projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + projetId
                ));

        task.setProjet(existingProject);
        return taskRepository.save(task);
    }

    public Task mettreAJourTache(Long id, Task taskDetails) {
        Task existingTask = recupererTacheParId(id);

        Long projetId = taskDetails.getProjet().getId();

        Project existingProject = projectRepository.findById(projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + projetId
                ));

        existingTask.setProjet(existingProject);
        existingTask.setResponsable(taskDetails.getResponsable());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setStatut(taskDetails.getStatut());
        existingTask.setDateLimite(taskDetails.getDateLimite());
        existingTask.setPriorite(taskDetails.getPriorite());

        return taskRepository.save(existingTask);
    }

    public void supprimerTache(Long id) {
        Task existingTask = recupererTacheParId(id);
        taskRepository.delete(existingTask);
    }
}