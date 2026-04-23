import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProjectService } from '../../services/project.services';
import { TaskService } from '../../services/task.services';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="dashboard-page">
      <div class="page-header">
        <div>
          <h1>Tableau de bord</h1>
          <p>Vue globale des projets, tâches et budgets</p>
        </div>

        <div class="header-actions">
          <a routerLink="/projets" class="secondary-button">Voir les projets</a>
          <a routerLink="/taches" class="secondary-button">Voir les tâches</a>
        </div>
      </div>

      <div *ngIf="messageErreur()" class="alert-card error">
        {{ messageErreur() }}
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <span class="stat-label">Projets</span>
          <h3>{{ projectService.totalProjets() }}</h3>
          <p>Nombre total de projets enregistrés</p>
        </div>

        <div class="stat-card">
          <span class="stat-label">Tâches</span>
          <h3>{{ taskService.totalTaches() }}</h3>
          <p>Nombre total de tâches enregistrées</p>
        </div>

        <div class="stat-card">
          <span class="stat-label">Budget total</span>
          <h3>{{ projectService.budgetTotal() | number:'1.2-2' }}</h3>
          <p>Somme des budgets de tous les projets</p>
        </div>
      </div>

      <div class="panel">
        <h2>Résumé</h2>
        <p *ngIf="projectService.totalProjets() === 0">
          Aucun projet n'est encore enregistré dans la base de données.
          La prochaine étape sera de créer le formulaire d'ajout de projet.
        </p>

        <p *ngIf="projectService.totalProjets() > 0">
          Des projets sont disponibles. Nous pourrons bientôt ajouter la gestion
          financière détaillée et l'attribution des ressources.
        </p>

        <div class="quick-actions">
          <a routerLink="/projets/nouveau" class="primary-button">Créer un projet</a>
          <a routerLink="/taches/nouveau" class="primary-button">Créer une tâche</a>
        </div>
      </div>
    </section>
  `,
  styles: [`
    .dashboard-page {
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
      margin-top: 8px;
      color: #6b7280;
    }

    .header-actions,
    .quick-actions {
      display: flex;
      gap: 12px;
      flex-wrap: wrap;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: 20px;
    }

    .stat-card {
      background: #ffffff;
      border-radius: 20px;
      padding: 24px;
      box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
      border: 1px solid #eef2f7;
    }

    .stat-label {
      display: inline-block;
      margin-bottom: 14px;
      color: #2563eb;
      font-size: 13px;
      font-weight: 700;
      text-transform: uppercase;
      letter-spacing: 0.08em;
    }

    .stat-card h3 {
      margin: 0;
      font-size: 32px;
      color: #111827;
    }

    .stat-card p {
      margin: 12px 0 0;
      color: #6b7280;
    }

    .panel {
      background: #ffffff;
      border-radius: 20px;
      padding: 24px;
      border: 1px solid #eef2f7;
      box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
    }

    .secondary-button,
    .primary-button {
      height: 42px;
      padding: 0 16px;
      border-radius: 12px;
      text-decoration: none;
      font-weight: 600;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      border: 1px solid transparent;
    }

    .secondary-button {
      background: #eff6ff;
      color: #1d4ed8;
      border-color: #bfdbfe;
    }

    .primary-button {
      background: linear-gradient(135deg, #2563eb, #1d4ed8);
      color: white;
    }

    .panel h2 {
      margin-top: 0;
      font-size: 20px;
    }

    .alert-card {
      padding: 16px 18px;
      border-radius: 14px;
      font-weight: 500;
    }

    .alert-card.error {
      background: #fef2f2;
      color: #b91c1c;
      border: 1px solid #fecaca;
    }
  `]
})
export class DashboardComponent implements OnInit {
  readonly projectService = inject(ProjectService);
  readonly taskService = inject(TaskService);

  readonly messageErreur = signal('');
  ngOnInit(): void {
    this.projectService.chargerTousLesProjets().subscribe({
      error: (error) => {
        console.error('Erreur lors du chargement des projets :', error);
        this.messageErreur.set(
          `Impossible de charger les projets. Code HTTP: ${error.status}`
        );
      }
    });

    this.taskService.chargerToutesLesTaches().subscribe({
      error: (error) => {
        console.error('Erreur lors du chargement des tâches :', error);
        this.messageErreur.set(
          `Impossible de charger les tâches. Code HTTP: ${error.status}`
        );
      }
    });
  }
}