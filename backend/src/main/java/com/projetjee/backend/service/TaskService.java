package com.projetjee.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.repository.TaskRepository;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Task;
import com.projetjee.backend.repository.ProjectRepository;

@Service
public class TaskService {

	private final ProjectRepository projectRepository;
	private final TaskRepository taskRepository;
	
	public TaskService(ProjectRepository projetRepository, TaskRepository tacheRepository) {
		this.projectRepository = projetRepository;
		this.taskRepository = tacheRepository;
	}
	
	public List<Task> getAllTasks(){
		return taskRepository.findAll();
	}
	public List<Task> getTasksByProjectId(Long projetId){
		return taskRepository.findByProjetId(projetId);
	}
	public Task getTaskById(Long id) {
		return taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tache introuvable id : "+id));
	}
	public Task createTask(Task task) {
		Long projetId = task.getProjet().getId();
		Project existingProject = projectRepository.findById(projetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Projet introuvable id : "+projetId));
		task.setProjet(existingProject);
		return taskRepository.save(task);
	}
	public Task updateTask(Long id, Task taskDetails) {
		Task existingTask = getTaskById(id);
		Long projetId = taskDetails.getProjet().getId();
		Project existingProject = projectRepository.findById(projetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Projet introuvable id : "+projetId));
		existingTask.setProjet(existingProject);
		existingTask.setResponsable(taskDetails.getResponsable());
		existingTask.setDescription(taskDetails.getDescription());
		existingTask.setStatus(taskDetails.getStatus());
		existingTask.setDateLimite(taskDetails.getDateLimite());
		existingTask.setPriorite(taskDetails.getPriorite());
		return taskRepository.save(existingTask);
	}
	public void deleteTask(Long id) {
		Task existingTask = getTaskById(id);
		taskRepository.delete(existingTask);		
}
}
