package com.projetjee.backend.config;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Supplier;

import org.springframework.context.annotation.Configuration;

import com.projetjee.backend.enums.ProjectStatus;
import com.projetjee.backend.enums.TaskStatus;
import com.projetjee.backend.repository.EmployeeRepository;
import com.projetjee.backend.repository.ProjectRepository;
import com.projetjee.backend.repository.TaskRepository;
import com.projetjee.backend.security.UserRepository;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class ProjectMetricsConfig {

    public ProjectMetricsConfig(
            MeterRegistry registry,
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            UserRepository userRepository) {

        registerGauge(registry, "gestiondeprojets_employees_total", "Total employees", () -> employeeRepository.count());
        registerGauge(registry, "gestiondeprojets_users_total", "Total application users", () -> userRepository.count());
        registerGauge(registry, "gestiondeprojets_projects_total", "Total projects", () -> projectRepository.count());
        registerGauge(registry, "gestiondeprojets_tasks_total", "Total tasks", () -> taskRepository.count());
        registerGauge(registry, "gestiondeprojets_project_budget_total", "Total planned project budget",
                () -> projectRepository.sumBudget());
        registerGauge(registry, "gestiondeprojets_task_estimated_cost_total", "Total estimated task cost",
                () -> taskRepository.sumCoutPrevu());
        registerGauge(registry, "gestiondeprojets_task_actual_cost_total", "Total actual task cost",
                () -> taskRepository.sumCoutReel());

        for (ProjectStatus status : ProjectStatus.values()) {
            Gauge.builder("gestiondeprojets_projects_by_status", projectRepository,
                    repository -> safeDouble(() -> repository.countByStatut(status)))
                    .description("Projects grouped by status")
                    .tag("status", metricLabel(status.name()))
                    .register(registry);
        }

        for (TaskStatus status : TaskStatus.values()) {
            Gauge.builder("gestiondeprojets_tasks_by_status", taskRepository,
                    repository -> safeDouble(() -> repository.countByStatut(status)))
                    .description("Tasks grouped by status")
                    .tag("status", metricLabel(status.name()))
                    .register(registry);
        }
    }

    private static void registerGauge(MeterRegistry registry, String name, String description, Supplier<Number> supplier) {
        Gauge.builder(name, supplier, ProjectMetricsConfig::safeDouble)
                .description(description)
                .register(registry);
    }

    private static double safeDouble(Supplier<Number> supplier) {
        try {
            Number value = supplier.get();
            return value == null ? 0 : value.doubleValue();
        } catch (RuntimeException ex) {
            return Double.NaN;
        }
    }

    private static String metricLabel(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");
    }
}
