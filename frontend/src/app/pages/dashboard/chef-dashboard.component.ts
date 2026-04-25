import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { Project } from '../../models/project.models';
import { ProjectService } from '../../services/project.services';

@Component({
  selector: 'app-chef-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Dashboard Chef de Projet</h1>
          <p>Vue limitée à vos projets visibles</p>
        </div>
      </div>

      <div *ngIf="messageErreur()" class="alert error">
        {{ messageErreur() }}
      </div>

      <div class="cards" *ngIf="!chargement()">
        <div class="card">
          <span>Mes projets</span>
          <h3>{{ totalProjets() }}</h3>
        </div>

        <div class="card">
          <span>En cours</span>
          <h3>{{ totalEnCours() }}</h3>
        </div>

        <div class="card">
          <span>Programmés</span>
          <h3>{{ totalProgrammes() }}</h3>
        </div>

        <div class="card">
          <span>Complétés</span>
          <h3>{{ totalCompletes() }}</h3>
        </div>
      </div>

      <div *ngIf="chargement()" class="loading-card">Chargement de vos projets...</div>

      <div *ngIf="!chargement() && projets().length === 0" class="empty-state">
        Aucun projet visible pour ce chef de projet.
      </div>

      <div *ngIf="!chargement() && projets().length > 0" class="table-card">
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
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let projet of projets()">
              <td>{{ projet.id }}</td>
              <td>{{ projet.nom }}</td>
              <td>{{ projet.dateDebut }}</td>
              <td>{{ projet.dateFin }}</td>
              <td>{{ projet.budget | number: '1.2-2' }}</td>
              <td>{{ projet.statut }}</td>
              <td>{{ projet.employes?.length || 0 }}</td>
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

      .page-header h1 {
        margin: 0;
        font-size: 28px;
        color: #111827;
      }

      .page-header p {
        margin: 8px 0 0;
        color: #6b7280;
      }

      .cards {
        display: grid;
        grid-template-columns: repeat(4, minmax(0, 1fr));
        gap: 20px;
      }

      .card,
      .table-card,
      .loading-card,
      .empty-state {
        background: #ffffff;
        border-radius: 24px;
        padding: 24px;
        border: 1px solid #eef2f7;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
      }

      .card span {
        display: block;
        color: #64748b;
        margin-bottom: 10px;
        font-size: 14px;
      }

      .card h3 {
        margin: 0;
        font-size: 28px;
        color: #111827;
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

      th {
        font-size: 13px;
        text-transform: uppercase;
        color: #64748b;
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

      @media (max-width: 992px) {
        .cards {
          grid-template-columns: repeat(2, minmax(0, 1fr));
        }
      }

      @media (max-width: 768px) {
        .cards {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class ChefDashboardComponent implements OnInit {
  private projectService = inject(ProjectService);

  readonly projets = signal<Project[]>([]);
  readonly chargement = signal(true);
  readonly messageErreur = signal('');

  readonly totalProjets = computed(() => this.projets().length);
  readonly totalEnCours = computed(
    () => this.projets().filter((p) => p.statut === 'En_Cours').length,
  );
  readonly totalProgrammes = computed(
    () => this.projets().filter((p) => p.statut === 'Programmé').length,
  );
  readonly totalCompletes = computed(
    () => this.projets().filter((p) => p.statut === 'Completé').length,
  );

  ngOnInit(): void {
    this.projectService.chargerMesProjetsChef().subscribe({
      next: (data) => {
        this.projets.set(data);
        this.chargement.set(false);
      },
      error: () => {
        this.messageErreur.set('Impossible de charger les projets du chef.');
        this.chargement.set(false);
      },
    });
  }
}
