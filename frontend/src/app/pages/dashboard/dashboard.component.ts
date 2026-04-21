import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { ProjectService } from '../../services/project.services';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  styleUrl: './dashboard.component.css',
  template: `
    <div class="container mt-4">
      <h2 class="mb-4">Tableau de bord</h2>

      <div *ngIf="errorMessage" class="alert alert-warning">
        {{ errorMessage }}
      </div>

      <div class="row g-3">
        <div class="col-md-4">
          <div class="card shadow-sm border-0">
            <div class="card-body">
              <h5 class="card-title">Projets</h5>
              <p class="display-6 mb-0">{{ totalProjects }}</p>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card shadow-sm border-0">
            <div class="card-body">
              <h5 class="card-title">Tâches</h5>
              <p class="display-6 mb-0">{{ totalTasks }}</p>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card shadow-sm border-0">
            <div class="card-body">
              <h5 class="card-title">Budget total</h5>
              <p class="display-6 mb-0">{{ totalBudget | number: '1.2-2' }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="alert alert-info mt-4">Cette page est la base du futur rapport financier.</div>
    </div>
  `,
})
export class DashboardComponent implements OnInit {
  private projectService = inject(ProjectService);
  private taskService = inject(TaskService);
  totalProjects = 0;
  totalTasks = 0;
  totalBudget = 0;
  errorMessage = '';

  ngOnInit(): void {
    forkJoin({
      projects: this.projectService.getAll(),
      tasks: this.taskService.getAll(),
    }).subscribe({
      next: ({ projects, tasks }) => {
        this.totalProjects = projects.length;
        this.totalTasks = tasks.length;
        this.totalBudget = projects.reduce((sum, project) => sum + Number(project.budget || 0), 0);
      },
      error: () => {
        this.errorMessage =
          'Impossible de charger les données. Vérifie que le backend tourne et que les URLs des endpoints sont correctes.';
      },
    });
  }
}
