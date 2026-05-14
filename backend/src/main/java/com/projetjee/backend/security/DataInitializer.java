package com.projetjee.backend.security;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.projetjee.backend.entity.Employee;
import com.projetjee.backend.entity.Project;
import com.projetjee.backend.entity.Ressource;
import com.projetjee.backend.entity.Task;
import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.enums.TaskPriority;
import com.projetjee.backend.enums.TaskStatus;
import com.projetjee.backend.repository.EmployeeRepository;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.RessourceRepository;
import com.projetjee.backend.repository.TaskRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final RessourceRepository ressourceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, EmployeeRepository employeeRepository,
            ProjectRepository projectRepository, TaskRepository taskRepository,
            RessourceRepository ressourceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.ressourceRepository = ressourceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Employee ala = ensureEmployee("Ala Fredj", "alafredj0@gmail.com", "Chef de projet", "Direction IT");
        Employee sara = ensureEmployee("Sara Ben Ali", "sara.benali@gestion.local", "Chef de projet", "Produit");
        Employee mehdi = ensureEmployee("Mehdi Trabelsi", "mehdi.trabelsi@gestion.local", "Developpeur", "Backend");
        Employee ines = ensureEmployee("Ines Mansouri", "ines.mansouri@gestion.local", "Analyste QA", "Qualite");
        Employee yassine = ensureEmployee("Yassine Karoui", "yassine.karoui@gestion.local", "DevOps", "Plateforme");
        Employee nour = ensureEmployee("Nour Haddad", "nour.haddad@gestion.local", "Analyste financier", "Finance");

        ensureUser("admin@admin.com", "admin123", Role.ADMIN, null);
        ensureUser("chef1@projet.com", "chef123", Role.CHEF_PROJET, ala);
        ensureUser("chef2@projet.com", "chef123", Role.CHEF_PROJET, sara);
        ensureUser("mehdi@projet.com", "employe123", Role.EMPLOYE, mehdi);
        ensureUser("ines@projet.com", "employe123", Role.EMPLOYE, ines);
        ensureUser("yassine@projet.com", "employe123", Role.EMPLOYE, yassine);
        ensureUser("nour@projet.com", "employe123", Role.EMPLOYE, nour);

        ensureRessource("Serveur cloud", "Materielle", "500.00", true);
        ensureRessource("Licences outils projet", "Logicielle", "1200.00", true);
        ensureRessource("Budget formation equipe", "Financiere", "2500.00", true);
        ensureRessource("Audit securite externe", "Financiere", "4000.00", true);

        LocalDate today = LocalDate.now();

        Project gitops = ensureProject(
                "Migration Kubernetes et GitOps",
                today.minusDays(20),
                today.plusDays(70),
                "42000.00",
                ProjectStatus.En_Cours,
                ala);
        assignEmployees(gitops, ala, sara, mehdi, ines, yassine);

        Project rhPortal = ensureProject(
                "Portail RH Ressources",
                today.minusDays(10),
                today.plusDays(90),
                "28000.00",
                ProjectStatus.Programmé,
                sara);
        assignEmployees(rhPortal, sara, mehdi, ines);

        Project finance = ensureProject(
                "Reporting Financier 2026",
                today.minusDays(35),
                today.plusDays(45),
                "35000.00",
                ProjectStatus.En_Cours,
                ala);
        assignEmployees(finance, ala, nour, yassine);

        Project archive = ensureProject(
                "Refonte Module Budget",
                today.minusDays(120),
                today.minusDays(15),
                "18000.00",
                ProjectStatus.Completé,
                sara);
        assignEmployees(archive, sara, nour, mehdi);

        ensureTask(gitops, "Configurer le cluster kind et les manifests Kustomize", ala, TaskStatus.Complété,
                TaskPriority.Elevé, today.plusDays(5), "4500.00", "4300.00");
        ensureTask(gitops, "Mettre en place Argo CD et le workflow GitOps", yassine, TaskStatus.En_Cours,
                TaskPriority.Elevé, today.plusDays(18), "7200.00", "3600.00");
        ensureTask(gitops, "Ajouter les dashboards Grafana du projet", ines, TaskStatus.En_Cours,
                TaskPriority.Moyenne, today.plusDays(25), "3100.00", "1200.00");

        ensureTask(rhPortal, "Concevoir les ecrans de gestion des employes", sara, TaskStatus.À_Faire,
                TaskPriority.Moyenne, today.plusDays(30), "5200.00", "0.00");
        ensureTask(rhPortal, "Implementer les endpoints RH", mehdi, TaskStatus.À_Faire,
                TaskPriority.Moyenne, today.plusDays(42), "6400.00", "0.00");

        ensureTask(finance, "Calculer les couts reels par projet", nour, TaskStatus.En_Cours,
                TaskPriority.Elevé, today.plusDays(12), "4800.00", "2100.00");
        ensureTask(finance, "Publier le rapport financier dans le dashboard", yassine, TaskStatus.À_Faire,
                TaskPriority.Faible, today.plusDays(28), "2900.00", "0.00");

        ensureTask(archive, "Migrer les donnees budget historiques", nour, TaskStatus.Complété,
                TaskPriority.Moyenne, today.plusDays(7), "3500.00", "3500.00");
    }

    private Employee ensureEmployee(String nom, String email, String role, String equipe) {
        return employeeRepository.findByEmail(email)
                .orElseGet(() -> employeeRepository.save(new Employee(nom, email, role, equipe)));
    }

    private User ensureUser(String email, String rawPassword, Role role, Employee employee) {
        return userRepository.findByEmail(email)
                .map(existingUser -> updateUserIfNeeded(existingUser, role, employee))
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setMotDePasse(passwordEncoder.encode(rawPassword));
                    user.setRole(role);
                    user.setEmploye(employee);
                    return userRepository.save(user);
                });
    }

    private User updateUserIfNeeded(User user, Role role, Employee employee) {
        boolean changed = false;

        if (user.getRole() != role) {
            user.setRole(role);
            changed = true;
        }

        Long currentEmployeeId = user.getEmploye() == null ? null : user.getEmploye().getId();
        Long targetEmployeeId = employee == null ? null : employee.getId();

        if (!java.util.Objects.equals(currentEmployeeId, targetEmployeeId)) {
            user.setEmploye(employee);
            changed = true;
        }

        return changed ? userRepository.save(user) : user;
    }

    private Ressource ensureRessource(String nom, String type, String cout, boolean disponibilite) {
        return ressourceRepository.findAll().stream()
                .filter(ressource -> nom.equals(ressource.getNom()))
                .findFirst()
                .orElseGet(() -> ressourceRepository.save(
                        new Ressource(nom, type, new BigDecimal(cout), disponibilite)
                ));
    }

    private Project ensureProject(String nom, LocalDate dateDebut, LocalDate dateFin, String budget,
            ProjectStatus statut, Employee chefProjet) {
        return projectRepository.findAll().stream()
                .filter(project -> nom.equals(project.getNom()))
                .findFirst()
                .orElseGet(() -> {
                    Project project = new Project();
                    project.setNom(nom);
                    project.setDateDebut(dateDebut);
                    project.setDateFin(dateFin);
                    project.setBudget(new BigDecimal(budget));
                    project.setStatut(statut);
                    project.setChefProjet(chefProjet);
                    return projectRepository.save(project);
                });
    }

    private void assignEmployees(Project project, Employee... employees) {
        List<Employee> assignedEmployees = new ArrayList<>(project.getEmployes());
        boolean changed = false;

        for (Employee employee : employees) {
            boolean alreadyAssigned = assignedEmployees.stream()
                    .anyMatch(assigned -> assigned.getId().equals(employee.getId()));

            if (!alreadyAssigned) {
                assignedEmployees.add(employee);
                changed = true;
            }
        }

        if (changed) {
            project.setEmployes(assignedEmployees);
            projectRepository.save(project);
        }
    }

    private void ensureTask(Project project, String description, Employee employee, TaskStatus statut,
            TaskPriority priorite, LocalDate dateLimite, String coutPrevu, String coutReel) {
        boolean exists = taskRepository.findByProjetId(project.getId()).stream()
                .anyMatch(task -> description.equals(task.getDescription()));

        if (exists) {
            return;
        }

        Task task = new Task();
        task.setProjet(project);
        task.setResponsable(employee.getNom());
        task.setDescription(description);
        task.setStatut(statut);
        task.setPriorite(priorite);
        task.setDateLimite(dateLimite);
        task.setCoutPrevu(new BigDecimal(coutPrevu));
        task.setCoutReel(new BigDecimal(coutReel));
        task.setEmployeAssigne(employee);

        taskRepository.save(task);
    }
}
