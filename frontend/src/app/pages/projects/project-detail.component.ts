import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { ProjectDetails } from '../../models/project.models';
import { ProjectService } from '../../services/project.services';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Détails du projet</h1>
          <p *ngIf="details">{{ details.projet.nom }}</p>
        </div>
        <a routerLink="/projets" class="secondary-button">Retour aux projets</a>
      </div>

      <div *ngIf="chargement" class="card">Chargement des détails...</div>
      <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>
      <div *ngIf="!chargement && !messageErreur && !details" class="card muted">
        Aucun détail disponible pour ce projet.
      </div>

      <ng-container *ngIf="details as d">
        <div class="summary-grid">
          <div class="card"><span>Budget</span><strong>{{ d.projet.budget | number: '1.2-2' }}</strong></div>
          <div class="card"><span>Cout tâches prévu</span><strong>{{ d.coutTachesPrevu | number: '1.2-2' }}</strong></div>
          <div class="card"><span>Cout ressources</span><strong>{{ d.coutRessources | number: '1.2-2' }}</strong></div>
          <div class="card"><span>Cout estimé</span><strong>{{ d.coutGlobal | number: '1.2-2' }}</strong></div>
        </div>

        <div class="card">
          <h2>Informations générales</h2>
          <div class="info-grid">
            <div><span>Nom</span><strong>{{ d.projet.nom }}</strong></div>
            <div><span>Date début</span><strong>{{ d.projet.dateDebut }}</strong></div>
            <div><span>Date fin</span><strong>{{ d.projet.dateFin }}</strong></div>
            <div><span>Statut</span><strong>{{ d.projet.statut }}</strong></div>
          </div>
        </div>

        <div class="card">
          <h2>Tâches</h2>
          <div *ngIf="d.taches.length === 0" class="muted">Aucune tâche pour ce projet.</div>
          <table *ngIf="d.taches.length > 0">
            <thead>
              <tr>
                <th>Description</th>
                <th>Statut</th>
                <th>Deadline</th>
                <th>Priorité</th>
                <th>Assigné à</th>
                <th>Cout prévu</th>
                <th>Cout réel</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let tache of d.taches">
                <td>{{ tache.description }}</td>
                <td>{{ tache.statut }}</td>
                <td>{{ tache.dateLimite }}</td>
                <td>{{ tache.priorite }}</td>
                <td>{{ tache.employeAssigne?.nom || 'Non assigné' }}</td>
                <td>{{ tache.coutPrevu | number: '1.2-2' }}</td>
                <td>{{ estCompletee(tache.statut) ? (tache.coutReel | number: '1.2-2') : '-' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="card">
          <h2>Ressources</h2>
          <div *ngIf="d.ressources.length === 0" class="muted">Aucune ressource affectée à ce projet.</div>
          <table *ngIf="d.ressources.length > 0">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Type</th>
                <th>Quantité</th>
                <th>Cout unitaire</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let ressource of d.ressources">
                <td>{{ ressource.nomRessource }}</td>
                <td>{{ ressource.typeRessource }}</td>
                <td>{{ ressource.quantite }}</td>
                <td>{{ ressource.coutUnitaire | number: '1.2-2' }}</td>
                <td>{{ ressource.coutTotal | number: '1.2-2' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="card">
          <h2>Équipe</h2>
          <div *ngIf="d.employes.length === 0" class="muted">Aucun employé affecté.</div>
          <div class="team-list" *ngIf="d.employes.length > 0">
            <div *ngFor="let employe of d.employes" class="team-chip">
              <strong>{{ employe.nom }}</strong>
              <span>{{ employe.role }} - {{ employe.equipe }}</span>
            </div>
          </div>
        </div>
      </ng-container>
    </section>
  `,
  styles: [
    `
      .page { display: flex; flex-direction: column; gap: 24px; }
      .page-header { display: flex; justify-content: space-between; gap: 16px; align-items: center; }
      .page-header h1 { margin: 0; color: #111827; }
      .page-header p { margin: 8px 0 0; color: #64748b; }
      .summary-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 16px; }
      .card { background: #fff; border: 1px solid #eef2f7; border-radius: 16px; padding: 20px; box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05); overflow-x: auto; }
      .card h2 { margin: 0 0 16px; font-size: 18px; }
      .card span { color: #64748b; display: block; margin-bottom: 6px; }
      .card strong { color: #111827; }
      .summary-grid .card strong { font-size: 22px; }
      .info-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 16px; }
      table { width: 100%; border-collapse: collapse; }
      th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eef2f7; }
      th { color: #64748b; text-transform: uppercase; font-size: 12px; }
      .team-list { display: flex; flex-wrap: wrap; gap: 12px; }
      .team-chip { border: 1px solid #e2e8f0; border-radius: 12px; padding: 12px 14px; background: #f8fafc; }
      .muted { color: #64748b; }
      .secondary-button { height: 42px; padding: 0 16px; border-radius: 12px; background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; text-decoration: none; display: inline-flex; align-items: center; font-weight: 600; }
      .alert { padding: 14px 16px; border-radius: 14px; font-weight: 500; }
      .error { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }
      @media (max-width: 900px) { .summary-grid, .info-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); } }
      @media (max-width: 640px) { .summary-grid, .info-grid { grid-template-columns: 1fr; } .page-header { flex-direction: column; align-items: flex-start; } }
    `,
  ],
})
export class ProjectDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private projectService = inject(ProjectService);
  private cdr = inject(ChangeDetectorRef);

  details: ProjectDetails | null = null;
  chargement = true;
  messageErreur = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (Number.isNaN(id) || id <= 0) {
      this.messageErreur = 'Identifiant de projet invalide.';
      this.chargement = false;
      return;
    }

    this.projectService
      .recupererDetailsProjet(id)
      .pipe(
        finalize(() => {
          this.chargement = false;
          this.cdr.detectChanges();
        }),
      )
      .subscribe({
        next: (details) => {
          this.details = details;
          this.messageErreur = '';
        },
        error: (error) => {
          this.details = null;
          this.messageErreur =
            error?.error?.message || error?.error?.error || 'Impossible de charger les détails du projet.';
        },
      });
  }

  estCompletee(statut: string): boolean {
    return statut?.toLowerCase().includes('compl');
  }
}
