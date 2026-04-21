import { CommonModule } from "@angular/common";
import { Component, inject, OnInit } from "@angular/core";
import { Project } from "../../models/project.models";
import { ProjectService } from "../../services/project.services";

@Component({
    selector: 'app-project-list',
    imports: [CommonModule],
    template: `
      <div class="container mt-4">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Liste des projets</h2>
      </div>

      <div *ngIf="errorMessage" class="alert alert-danger">
        {{ errorMessage }}
      </div>

      <div *ngIf="projects.length === 0 && !errorMessage" class="alert alert-info">
        Aucun projet trouvé.
      </div>

      <div class="table-responsive" *ngIf="projects.length > 0">
        <table class="table table-bordered table-hover align-middle">
          <thead class="table-dark">
            <tr>
              <th>ID</th>
              <th>Nom</th>
              <th>Date début</th>
              <th>Date fin</th>
              <th>Budget</th>
              <th>Statut</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let project of projects">
              <td>{{ project.id }}</td>
              <td>{{ project.nom }}</td>
              <td>{{ project.dateDebut }}</td>
              <td>{{ project.dateFin }}</td>
              <td>{{ project.budget | number:'1.2-2' }}</td>
              <td>{{ project.statut }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class ProjectListComponent implements OnInit{
    private projectService = inject(ProjectService);
    projects: Project[] = [];
    errorMessage = '';
    ngOnInit(): void {
        this.projectService.getAll().subscribe({
            next: (data) => {this.projects = data;
            },
            error: () => {
                this.errorMessage='Impossible de charger les projets. Vérifie le backend et le mapping du controller.';
            }
        })
    }
}
            