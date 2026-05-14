package com.projetjee.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Task;
import com.projetjee.backend.repository.EmployeeRepository;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.TaskRepository;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public TaskService(ProjectRepository projectRepository, TaskRepository taskRepository,
            EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
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
                        "Tache introuvable avec l'id : " + id
                ));
    }

    @Transactional
    public Task creerTache(Task task) {
        Long projetId = extraireProjetId(task);

        Project existingProject = projectRepository.findById(projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + projetId
                ));

        validerResponsableChefProjet(task.getResponsable());

        task.setProjet(existingProject);
        if (task.getCoutReel() == null) {
            task.setCoutReel(BigDecimal.ZERO);
        }
        if (task.getCoutPrevu() == null) {
            task.setCoutPrevu(BigDecimal.ZERO);
        }
        normaliserCoutReelSelonStatut(task);
        task.setEmployeAssigne(resoudreEmployeAssigne(task, existingProject));
        Task tacheSauvegardee = taskRepository.save(task);
        ajusterBudgetProjet(existingProject, valeurOuZero(tacheSauvegardee.getCoutPrevu()));
        return tacheSauvegardee;
    }

    @Transactional
    public Task mettreAJourTache(Long id, Task taskDetails) {
        Task existingTask = recupererTacheParId(id);
        Project ancienProjet = existingTask.getProjet();
        BigDecimal ancienCoutPrevu = valeurOuZero(existingTask.getCoutPrevu());
        Long projetId = extraireProjetId(taskDetails);

        Project existingProject = projectRepository.findById(projetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projet introuvable avec l'id : " + projetId
                ));

        validerResponsableChefProjet(taskDetails.getResponsable());

        existingTask.setProjet(existingProject);
        existingTask.setResponsable(taskDetails.getResponsable());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setStatut(taskDetails.getStatut());
        existingTask.setDateLimite(taskDetails.getDateLimite());
        existingTask.setPriorite(taskDetails.getPriorite());
        existingTask.setCoutPrevu(taskDetails.getCoutPrevu());
        existingTask.setCoutReel(taskDetails.getCoutReel());
        normaliserCoutReelSelonStatut(existingTask);
        existingTask.setEmployeAssigne(resoudreEmployeAssigne(taskDetails, existingProject));

        Task tacheSauvegardee = taskRepository.save(existingTask);
        BigDecimal nouveauCoutPrevu = valeurOuZero(tacheSauvegardee.getCoutPrevu());

        if (ancienProjet != null && !ancienProjet.getId().equals(existingProject.getId())) {
            ajusterBudgetProjet(ancienProjet, ancienCoutPrevu.negate());
            ajusterBudgetProjet(existingProject, nouveauCoutPrevu);
        } else {
            ajusterBudgetProjet(existingProject, nouveauCoutPrevu.subtract(ancienCoutPrevu));
        }

        return tacheSauvegardee;
    }

    @Transactional
    public void supprimerTache(Long id) {
        Task existingTask = recupererTacheParId(id);
        Project projet = existingTask.getProjet();
        BigDecimal coutASoustraire = valeurOuZero(existingTask.getCoutPrevu()).negate();
        taskRepository.delete(existingTask);
        if (projet != null) {
            ajusterBudgetProjet(projet, coutASoustraire);
        }
    }

    private Long extraireProjetId(Task task) {
        if (task == null || task.getProjet() == null || task.getProjet().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le projet de la tache est obligatoire."
            );
        }

        return task.getProjet().getId();
    }

    private void validerResponsableChefProjet(String responsable) {
        if (responsable == null || responsable.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le responsable de la tache est obligatoire."
            );
        }

        boolean responsableValide = employeeRepository.existsByNomAndRoleIgnoreCase(responsable, "Chef de projet");
        if (!responsableValide) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le responsable doit etre un employe avec le role Chef de projet."
            );
        }
    }

    private Employee resoudreEmployeAssigne(Task task, Project projet) {
        if (task.getEmployeAssigne() == null || task.getEmployeAssigne().getId() == null) {
            return null;
        }

        Long employeId = task.getEmployeAssigne().getId();

        Employee employe = employeeRepository.findById(employeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Employe assigne introuvable avec l'id : " + employeId
                ));

        boolean membreProjet = projet.getEmployes() != null
                && projet.getEmployes().stream().anyMatch(membre -> employeId.equals(membre.getId()));

        if (!membreProjet) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'employe assigne doit appartenir a l'equipe du projet."
            );
        }

        return employe;
    }

    private void normaliserCoutReelSelonStatut(Task task) {
        if (task.getStatut() == null || !task.getStatut().name().contains("Compl")) {
            task.setCoutReel(BigDecimal.ZERO);
            return;
        }

        if (task.getCoutReel() == null) {
            task.setCoutReel(BigDecimal.ZERO);
        }
    }

    private void ajusterBudgetProjet(Project projet, BigDecimal delta) {
        BigDecimal deltaBudget = valeurOuZero(delta);
        if (deltaBudget.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        projet.setBudget(valeurOuZero(projet.getBudget()).add(deltaBudget));
        projectRepository.save(projet);
    }

    private BigDecimal valeurOuZero(BigDecimal valeur) {
        return valeur != null ? valeur : BigDecimal.ZERO;
    }
}
