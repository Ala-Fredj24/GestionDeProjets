package com.projetjee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projetjee.backend.entity.Task;
import com.projetjee.backend.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/taches")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> creerTache(@RequestBody @Valid Task task) {
        Task createdTask = taskService.creerTache(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Task> recupererToutesLesTaches() {
        return taskService.recupererToutesLesTaches();
    }

    @GetMapping("/{id}")
    public Task recupererTacheParId(@PathVariable Long id) {
        return taskService.recupererTacheParId(id);
    }

    @GetMapping("/projet/{projetId}")
    public List<Task> recupererTachesParProjetId(@PathVariable Long projetId) {
        return taskService.recupererTachesParProjetId(projetId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> mettreAJourTache(@PathVariable Long id, @Valid @RequestBody Task task) {
        Task updatedTask = taskService.mettreAJourTache(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTache(@PathVariable Long id) {
        taskService.supprimerTache(id);
        return ResponseEntity.noContent().build();
    }
}