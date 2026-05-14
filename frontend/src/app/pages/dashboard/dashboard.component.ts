import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RapportFinancierProjet } from '../../models/rapport-financier.models';
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
          <p>Vue globale des projets, taches et budgets</p>
        </div>

        <div class="header-actions">
          <a routerLink="/projets" class="secondary-button">Voir les projets</a>
          <a routerLink="/taches" class="secondary-button">Voir les taches</a>
        </div>
      </div>

      <div *ngIf="messageErreur()" class="alert-card error">
        {{ messageErreur() }}
      </div>

      <div class="panel quick-panel">
        <h2>Actions rapides</h2>
        <div class="quick-actions">
          <a routerLink="/projets/nouveau" class="primary-button">Creer un projet</a>
          <a routerLink="/taches/nouveau" class="primary-button">Creer une tache</a>
        </div>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <span class="stat-label">Projets</span>
          <h3>{{ projectService.totalProjets() }}</h3>
          <p>Nombre total de projets enregistres</p>
        </div>

        <div class="stat-card">
          <span class="stat-label">Taches</span>
          <h3>{{ taskService.totalTaches() }}</h3>
          <p>Nombre total de taches enregistrees</p>
        </div>

        <div class="stat-card">
          <span class="stat-label">Budget total</span>
          <h3>{{ projectService.budgetTotal() | number: '1.2-2' }}</h3>
          <p>Somme des budgets de tous les projets</p>
        </div>
      </div>

      <section class="project-summary">
        <div class="section-title">
          <h2>Resume des projets</h2>
          <p>Statistiques calculees depuis les rapports financiers backend.</p>
        </div>

        <div *ngIf="chargementRapports()" class="loading-card">Chargement des statistiques projets...</div>

        <div *ngIf="!chargementRapports() && rapports().length === 0" class="empty-state">
          Aucun projet a afficher.
        </div>

        <div *ngIf="!chargementRapports() && rapports().length > 0" class="project-grid">
          <article class="project-card" *ngFor="let rapport of rapports()">
            <div class="card-header">
              <h3>{{ rapport.nomProjet }}</h3>
              <span class="badge status">{{ rapport.statutProjet }}</span>
            </div>

            <div class="metrics">
              <div>
                <span>Budget</span>
                <strong>{{ rapport.budgetProjet | number: '1.2-2' }}</strong>
              </div>
              <div>
                <span>Cout estime</span>
                <strong>{{ coutEstimeProjet(rapport) | number: '1.2-2' }}</strong>
              </div>
              <div>
                <span>Taches</span>
                <strong>{{ rapport.nombreTaches }}</strong>
              </div>
            </div>

            <div>
              <div class="bar-label">
                <span>Cout estime / budget</span>
                <strong>{{ tauxCoutEstime(rapport) | number: '1.0-2' }} %</strong>
              </div>
              <div class="completion-bar" [class.over]="depassementBudget(rapport) > 0" aria-hidden="true">
                <span [style.width.%]="largeurBarreCout(rapport)"></span>
              </div>
            </div>

            <div class="overrun" *ngIf="depassementBudget(rapport) > 0">
              Depassement: {{ depassementBudget(rapport) | number: '1.2-2' }}
            </div>

            <div class="mastery" *ngIf="projetComplete(rapport); else nonEvalue">
              <div>
                <span>Cout reel projet</span>
                <strong>{{ coutReelProjet(rapport) | number: '1.2-2' }}</strong>
              </div>
              <span class="badge" [class.ok]="etatMaitrise(rapport) === 'Maitrise'" [class.depasse]="etatMaitrise(rapport) === 'Depassement'">
                {{ etatMaitrise(rapport) }}
              </span>
            </div>

            <ng-template #nonEvalue>
              <div class="mastery">
                <div>
                  <span>Cout reel projet</span>
                  <strong>Non evalue</strong>
                </div>
                <span class="badge neutral">Non evalue</span>
              </div>
            </ng-template>
          </article>
        </div>
      </section>

    </section>
  `,
  styles: [
    `
      .dashboard-page {
        display: flex;
        flex-direction: column;
        gap: 24px;
      }

      .page-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 16px;
      }

      .page-header h1,
      .section-title h2 {
        margin: 0;
        color: #111827;
      }

      .page-header h1 {
        font-size: 28px;
      }

      .page-header p,
      .section-title p {
        margin: 8px 0 0;
        color: #6b7280;
      }

      .header-actions,
      .quick-actions {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
      }

      .stats-grid,
      .project-grid {
        display: grid;
        gap: 20px;
      }

      .stats-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
      }

      .project-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
      }

      .stat-card,
      .project-card,
      .panel,
      .loading-card,
      .empty-state {
        background: #ffffff;
        border-radius: 16px;
        padding: 24px;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
        border: 1px solid #eef2f7;
      }

      .stat-label,
      .metrics span,
      .mastery span:first-child {
        color: #64748b;
        font-size: 13px;
        font-weight: 700;
        text-transform: uppercase;
      }

      .stat-label {
        display: inline-block;
        margin-bottom: 14px;
        color: #2563eb;
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

      .project-summary {
        display: flex;
        flex-direction: column;
        gap: 16px;
      }

      .project-card {
        display: flex;
        flex-direction: column;
        gap: 18px;
      }

      .card-header,
      .mastery {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 12px;
      }

      .card-header h3 {
        margin: 0;
        font-size: 18px;
        color: #111827;
      }

      .metrics {
        display: grid;
        grid-template-columns: repeat(3, minmax(0, 1fr));
        gap: 12px;
      }

      .metrics div,
      .mastery div {
        display: flex;
        flex-direction: column;
        gap: 6px;
      }

      strong {
        color: #111827;
      }

      .completion-bar {
        height: 8px;
        overflow: hidden;
        border-radius: 999px;
        background: #e2e8f0;
      }

      .completion-bar span {
        display: block;
        height: 100%;
        background: #2563eb;
      }

      .completion-bar.over span {
        background: #dc2626;
      }

      .bar-label {
        display: flex;
        justify-content: space-between;
        gap: 12px;
        margin-bottom: 8px;
      }

      .bar-label span {
        color: #64748b;
        font-size: 13px;
        font-weight: 700;
        text-transform: uppercase;
      }

      .overrun {
        color: #b91c1c;
        background: #fef2f2;
        border: 1px solid #fecaca;
        border-radius: 12px;
        padding: 10px 12px;
        font-weight: 700;
      }

      .badge {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        min-height: 30px;
        padding: 0 10px;
        border-radius: 999px;
        font-size: 12px;
        font-weight: 700;
        white-space: nowrap;
      }

      .badge.status,
      .badge.neutral {
        background: #f1f5f9;
        color: #334155;
      }

      .badge.ok {
        background: #ecfdf5;
        color: #047857;
      }

      .badge.depasse {
        background: #fef2f2;
        color: #b91c1c;
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
        background: #2563eb;
        color: white;
      }

      .panel h2 {
        margin-top: 0;
        font-size: 20px;
      }

      .quick-panel {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 16px;
        flex-wrap: wrap;
      }

      .quick-panel h2 {
        margin: 0;
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

      @media (max-width: 1100px) {
        .project-grid {
          grid-template-columns: repeat(2, minmax(0, 1fr));
        }
      }

      @media (max-width: 768px) {
        .page-header {
          flex-direction: column;
        }

        .stats-grid,
        .project-grid {
          grid-template-columns: 1fr;
        }

        .metrics {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class DashboardComponent implements OnInit {
  readonly projectService = inject(ProjectService);
  readonly taskService = inject(TaskService);

  readonly rapports = signal<RapportFinancierProjet[]>([]);
  readonly chargementRapports = signal(true);
  readonly messageErreur = signal('');

  ngOnInit(): void {
    this.projectService.chargerTousLesProjets().subscribe({
      error: (error) => {
        console.error('Erreur lors du chargement des projets :', error);
        this.messageErreur.set(`Impossible de charger les projets. Code HTTP: ${error.status}`);
      },
    });

    this.taskService.chargerToutesLesTaches().subscribe({
      error: (error) => {
        console.error('Erreur lors du chargement des taches :', error);
        this.messageErreur.set(`Impossible de charger les taches. Code HTTP: ${error.status}`);
      },
    });

    this.projectService.chargerTousLesRapportsFinanciers().subscribe({
      next: (rapports) => {
        this.rapports.set(rapports);
        this.chargementRapports.set(false);
      },
      error: () => {
        this.messageErreur.set('Impossible de charger les statistiques par projet.');
        this.chargementRapports.set(false);
      },
    });
  }

  projetComplete(rapport: RapportFinancierProjet): boolean {
    return (rapport.statutProjet || '').toLowerCase().includes('compl');
  }

  coutReelProjet(rapport: RapportFinancierProjet): number {
    return Number(rapport.coutReelTotal || 0) + Number(rapport.coutRessourcesTotal || 0);
  }

  coutEstimeProjet(rapport: RapportFinancierProjet): number {
    return Number(rapport.coutGlobalTotal || 0);
  }

  tauxCoutEstime(rapport: RapportFinancierProjet): number {
    const budget = Number(rapport.budgetProjet || 0);
    if (budget <= 0) {
      return 0;
    }

    return (this.coutEstimeProjet(rapport) / budget) * 100;
  }

  largeurBarreCout(rapport: RapportFinancierProjet): number {
    return Math.min(this.tauxCoutEstime(rapport), 100);
  }

  depassementBudget(rapport: RapportFinancierProjet): number {
    return Math.max(this.coutEstimeProjet(rapport) - Number(rapport.budgetProjet || 0), 0);
  }

  etatMaitrise(rapport: RapportFinancierProjet): 'Maitrise' | 'Depassement' | 'Non evalue' {
    if (!this.projetComplete(rapport)) {
      return 'Non evalue';
    }

    return this.coutReelProjet(rapport) <= Number(rapport.budgetProjet || 0) ? 'Maitrise' : 'Depassement';
  }
}
