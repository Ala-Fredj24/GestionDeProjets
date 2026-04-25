import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { RapportFinancierProjet } from '../../models/rapport-financier.models';
import { ProjectService } from '../../services/project.services';

@Component({
  selector: 'app-financial-report',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Rapport financier</h1>
          <p>Suivi du budget global et des coûts par projet</p>
        </div>
      </div>

      <div *ngIf="messageErreur()" class="alert error">
        {{ messageErreur() }}
      </div>

      <div class="cards" *ngIf="!chargement()">
        <div class="card">
          <span>Budget global total</span>
          <h3>{{ totalBudgetProjet() | number: '1.2-2' }}</h3>
        </div>

        <div class="card">
          <span>Coût prévu total</span>
          <h3>{{ totalCoutPrevu() | number: '1.2-2' }}</h3>
        </div>

        <div class="card">
          <span>Coût réel total</span>
          <h3>{{ totalCoutReel() | number: '1.2-2' }}</h3>
        </div>

        <div class="card">
          <span>Projets en dépassement</span>
          <h3>{{ totalProjetsEnDepassement() }}</h3>
        </div>
      </div>

      <div *ngIf="chargement()" class="loading-card">Chargement du rapport financier...</div>

      <div *ngIf="!chargement() && rapports().length === 0" class="empty-state">
        Aucun rapport financier disponible.
      </div>

      <div *ngIf="!chargement() && rapports().length > 0" class="table-card">
        <table>
          <thead>
            <tr>
              <th>Projet</th>
              <th>Statut</th>
              <th>Budget projet</th>
              <th>Coût prévu total</th>
              <th>Coût réel total</th>
              <th>Écart</th>
              <th>Reste budget</th>
              <th>Taux consommation</th>
              <th>Nb tâches</th>
              <th>État</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let rapport of rapports()">
              <td>{{ rapport.nomProjet }}</td>
              <td>{{ rapport.statutProjet }}</td>
              <td>{{ rapport.budgetProjet | number: '1.2-2' }}</td>
              <td>{{ rapport.coutPrevuTotal | number: '1.2-2' }}</td>
              <td>{{ rapport.coutReelTotal | number: '1.2-2' }}</td>
              <td>{{ rapport.ecartPrevuReel | number: '1.2-2' }}</td>
              <td>{{ rapport.resteBudget | number: '1.2-2' }}</td>
              <td>{{ rapport.tauxConsommation | number: '1.2-2' }} %</td>
              <td>{{ rapport.nombreTaches }}</td>
              <td>
                <span class="badge depasse" *ngIf="rapport.depasseBudget">Dépassé</span>
                <span class="badge ok" *ngIf="!rapport.depasseBudget">Maîtrisé</span>
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

      .badge {
        display: inline-block;
        padding: 8px 12px;
        border-radius: 999px;
        font-size: 12px;
        font-weight: 700;
      }

      .badge.ok {
        background: #ecfdf5;
        color: #047857;
      }

      .badge.depasse {
        background: #fef2f2;
        color: #b91c1c;
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
export class FinancialReportComponent implements OnInit {
  readonly projectService = inject(ProjectService);

  readonly rapports = signal<RapportFinancierProjet[]>([]);
  readonly chargement = signal(true);
  readonly messageErreur = signal('');

  readonly totalBudgetProjet = computed(() =>
    this.rapports().reduce((somme, rapport) => somme + Number(rapport.budgetProjet || 0), 0),
  );

  readonly totalCoutPrevu = computed(() =>
    this.rapports().reduce((somme, rapport) => somme + Number(rapport.coutPrevuTotal || 0), 0),
  );

  readonly totalCoutReel = computed(() =>
    this.rapports().reduce((somme, rapport) => somme + Number(rapport.coutReelTotal || 0), 0),
  );

  readonly totalProjetsEnDepassement = computed(
    () => this.rapports().filter((rapport) => rapport.depasseBudget).length,
  );

  ngOnInit(): void {
    this.projectService.chargerTousLesRapportsFinanciers().subscribe({
      next: (data) => {
        this.rapports.set(data);
        this.chargement.set(false);
      },
      error: () => {
        this.messageErreur.set('Impossible de charger le rapport financier.');
        this.chargement.set(false);
      },
    });
  }
}
