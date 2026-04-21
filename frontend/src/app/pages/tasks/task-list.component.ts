import { CommonModule } from "@angular/common";
import { Component, OnInit, inject } from "@angular/core";
import { TaskService } from "../../services/task.service";
import { Task } from "../../models/task.models";

@Component({
    selector: 'app-task-list',
    standalone: true,
    imports: [CommonModule],
    template: `
      <div class="container mt-4">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Liste des tâches</h2>
      </div>

      <div *ngIf="errorMessage" class="alert alert-danger">
        {{ errorMessage }}
      </div>

      <div *ngIf="tasks.length === 0 && !errorMessage" class="alert alert-info">
        Aucune tâche trouvée.
      </div>

      <div class="table-responsive" *ngIf="tasks.length > 0">
        <table class="table table-bordered table-hover align-middle">
          <thead class="table-dark">
            <tr>
              <th>ID</th>
              <th>Projet</th>
              <th>Description</th>
              <th>Responsable</th>
              <th>Statut</th>
              <th>Priorité</th>
              <th>Date limite</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let task of tasks">
              <td>{{ task.id }}</td>
              <td>{{ task.projet?.nom || 'Non défini' }}</td>
              <td>{{ task.description }}</td>
              <td>{{ task.responsable }}</td>
              <td>{{ task.statut }}</td>
              <td>{{ task.priorite }}</td>
              <td>{{ task.dateLimite }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class TaskListComponent implements OnInit{
    private taskService = inject(TaskService);
    tasks: Task[] = [];
    errorMessage = '';
    ngOnInit(): void {
        this.taskService.getAll().subscribe({
            next: (data) => {this.tasks = data;
            }, 
            error: () => {
                this.errorMessage='Impossible de charger les tâches. Vérifie le backend et le mapping du controller.';
            }
        })
    }
}
