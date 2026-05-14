import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TaskService } from '../../services/task.services';
import { Task } from '../../models/task.models';
import { ProjectService } from '../../services/project.services';
import { Project } from '../../models/project.models';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Tâches</h1>
          <p>{{ projet ? 'Tâches du projet ' + projet.nom : 'Suivi opérationnel des tâches du projet' }}</p>
        </div>

        <div class="header-actions">
          <a *ngIf="projet" routerLink="/projets" class="secondary-button">Retour aux projets</a>
          <a
            routerLink="/taches/nouveau"
            [queryParams]="projet ? { projetId: projet.id } : null"
            class="primary-button"
          >
            Nouvelle tâche
          </a>
        </div>
      </div>

      <div *ngIf="messageErreur" class="alert error">
        {{ messageErreur }}
      </div>

      <div *ngIf="taskService.taches().length === 0 && !messageErreur" class="empty-state">
        <div class="icon">✅</div>
        <h2>Aucune tâche disponible</h2>
        <p>Crée une tâche pour commencer le suivi opérationnel.</p>
      </div>

      <div *ngIf="taskService.taches().length > 0" class="table-card">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Projet</th>
              <th>Description</th>
              <th>Responsable</th>
              <th>Statut</th>
              <th>Priorité</th>
              <th>Date limite</th>
              <th>Coût prévu</th>
              <th>Cout réel</th>
              <th>Employé assigné</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let tache of taskService.taches()">
              <td>{{ tache.id }}</td>
              <td>{{ tache.projet?.nom }}</td>
              <td>{{ tache.description }}</td>
              <td>{{ tache.responsable }}</td>
              <td>{{ tache.statut }}</td>
              <td>{{ tache.priorite }}</td>
              <td>{{ tache.dateLimite }}</td>
              <td>{{ tache.coutPrevu | number: '1.2-2' }}</td>
              <td>{{ tache.coutReel | number: '1.2-2' }}</td>
              <td>{{ tache.employeAssigne?.nom || 'Non assigné' }}</td>
              <td class="actions">
                <a
                  class="icon-button link-button"
                  [routerLink]="['/taches', tache.id, 'modifier']"
                  aria-label="Modifier la tâche"
                  title="Modifier"
                >
                  ✏️
                </a>
                <button
                  type="button"
                  class="icon-button danger-button"
                  (click)="supprimerTache(tache)"
                  aria-label="Supprimer la tâche"
                  title="Supprimer"
                >
                  🗑️
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  `,
  styles: [
    `
      .page {
        display: flex;
        flex-direction: column;
        gap: 24px;
      }

      .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 16px;
      }

      .page-header h1 {
        margin: 0;
        font-size: 28px;
        color: #111827;
      }

      .page-header p {
        margin: 8px 0 0;
        color: #6b7280;
      }

      .table-card,
      .empty-state {
        background: #ffffff;
        border-radius: 24px;
        padding: 24px;
        border: 1px solid #eef2f7;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
      }

      .empty-state {
        text-align: center;
        padding: 50px 24px;
      }

      .icon {
        font-size: 46px;
        margin-bottom: 10px;
      }

      table {
        width: 100%;
        border-collapse: collapse;
      }

      th,
      td {
        padding: 16px;
        text-align: left;
        border-bottom: 1px solid #eef2f7;
      }

      .actions {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
      }

      th {
        font-size: 13px;
        text-transform: uppercase;
        color: #64748b;
      }

      .primary-button {
        height: 44px;
        padding: 0 18px;
        border-radius: 12px;
        text-decoration: none;
        font-weight: 600;
        display: inline-flex;
        align-items: center;
        background: linear-gradient(135deg, #2563eb, #1d4ed8);
        color: white;
      }

      .secondary-button {
        height: 44px;
        padding: 0 18px;
        border-radius: 12px;
        text-decoration: none;
        font-weight: 600;
        display: inline-flex;
        align-items: center;
        background: #eff6ff;
        color: #1d4ed8;
        border: 1px solid #bfdbfe;
      }

      .header-actions {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
      }

      .link-button,
      .danger-button {
        height: 38px;
        padding: 0 14px;
        border-radius: 10px;
        border: 1px solid transparent;
        cursor: pointer;
        font-weight: 600;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
      }

      .icon-button {
        width: 38px;
        padding: 0;
        font-size: 16px;
      }

      .link-button {
        background: #eff6ff;
        color: #1d4ed8;
        border-color: #bfdbfe;
      }

      .danger-button {
        background: #fef2f2;
        color: #b91c1c;
        border-color: #fecaca;
      }

      .alert {
        padding: 14px 16px;
        border-radius: 14px;
        font-weight: 500;
      }

      .error {
        background: #fef2f2;
        color: #b91c1c;
        border: 1px solid #fecaca;
      }
    `,
  ],
})
export class TaskListComponent implements OnInit {
  readonly taskService = inject(TaskService);
  private projectService = inject(ProjectService);
  private route = inject(ActivatedRoute);

  projet: Project | null = null;
  messageErreur = '';

  ngOnInit(): void {
    const projetId = Number(this.route.snapshot.paramMap.get('id'));

    if (!Number.isNaN(projetId) && projetId > 0) {
      this.chargerProjet(projetId);
      this.chargerTachesProjet(projetId);
      return;
    }

    this.chargerToutesLesTaches();
  }

  private chargerToutesLesTaches(): void {
    this.taskService.chargerToutesLesTaches().subscribe({
      error: (error) => {
        this.messageErreur =
          `Impossible de charger les tâches. ${error?.status ?? ''} ${error?.statusText ?? ''}`.trim();
      },
    });
  }

  private chargerProjet(projetId: number): void {
    this.projectService.recupererProjetParId(projetId).subscribe({
      next: (projet) => {
        this.projet = projet;
      },
      error: () => {
        this.messageErreur = 'Impossible de charger le projet.';
      },
    });
  }

  private chargerTachesProjet(projetId: number): void {
    this.taskService.chargerTachesParProjet(projetId).subscribe({
      error: (error) => {
        this.messageErreur =
          `Impossible de charger les tâches du projet. ${error?.status ?? ''} ${error?.statusText ?? ''}`.trim();
      },
    });
  }

  supprimerTache(tache: Task): void {
    if (tache.id == null) {
      return;
    }

    const confirme = confirm(`Supprimer la tâche "${tache.description}" ?`);
    if (!confirme) {
      return;
    }

    this.taskService.supprimerTache(tache.id).subscribe({
      next: () => {
        if (this.projet?.id) {
          this.taskService.chargerTachesParProjet(this.projet.id).subscribe();
          return;
        }

        this.taskService.chargerToutesLesTaches().subscribe();
      },
      error: (error) => {
        this.messageErreur =
          `Erreur suppression tâche : ${error?.status ?? ''} ${error?.statusText ?? ''}`.trim();
      },
    });
  }
}
