import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../services/project.services';
import { Project } from '../../models/project.models';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Projets</h1>
          <p>Liste des projets enregistrés dans la plateforme</p>
        </div>

        <a routerLink="/projets/nouveau" class="primary-button">Nouveau projet</a>
      </div>

      <div *ngIf="messageErreur" class="alert error">
        {{ messageErreur }}
      </div>

      <div *ngIf="projectService.projets().length === 0 && !messageErreur" class="empty-state">
        <div class="icon">📁</div>
        <h2>Aucun projet disponible</h2>
        <p>Commence par créer ton premier projet.</p>
      </div>

      <div *ngIf="projectService.projets().length > 0" class="table-card">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nom</th>
              <th>Date début</th>
              <th>Date fin</th>
              <th>Budget</th>
              <th>Statut</th>
              <th>Ressources</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let projet of projectService.projets()">
              <td>{{ projet.id }}</td>
              <td>{{ projet.nom }}</td>
              <td>{{ projet.dateDebut }}</td>
              <td>{{ projet.dateFin }}</td>
              <td>{{ projet.budget | number: '1.2-2' }}</td>
              <td>{{ projet.statut }}</td>
              <td>{{ projet.employes?.length || 0 }}</td>
              <td>
                <a [routerLink]="['/projets', projet.id, 'ressources']" class="secondary-button">
                  Gérer les ressources
                </a>
              </td>
              <td class="actions">
                <a
                  class="icon-button link-button"
                  [routerLink]="['/projets', projet.id, 'modifier']"
                  aria-label="Modifier le projet"
                  title="Modifier"
                >
                  ✏️
                </a>
                <button
                  type="button"
                  class="icon-button danger-button"
                  (click)="supprimerProjet(projet)"
                  aria-label="Supprimer le projet"
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
export class ProjectListComponent implements OnInit {
  readonly projectService = inject(ProjectService);
  messageErreur = '';

  ngOnInit(): void {
    this.projectService.chargerTousLesProjets().subscribe({
      error: (error) => {
        this.messageErreur =
          `Impossible de charger les projets. ${error?.status ?? ''} ${error?.statusText ?? ''}`.trim();
      },
    });
  }

  supprimerProjet(projet: Project): void {
    if (projet.id == null) {
      return;
    }

    const confirme = confirm(`Supprimer le projet "${projet.nom}" ?`);
    if (!confirme) {
      return;
    }

    this.projectService.supprimerProjet(projet.id).subscribe({
      next: () => {
        this.projectService.chargerTousLesProjets().subscribe();
      },
      error: (error) => {
        this.messageErreur =
          `Erreur suppression projet : ${error?.status ?? ''} ${error?.statusText ?? ''}`.trim();
      },
    });
  }
}
