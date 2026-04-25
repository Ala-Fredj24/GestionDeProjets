import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Employee } from '../../models/employee.models';
import { Project } from '../../models/project.models';
import { EmployeeService } from '../../services/employee.service';
import { ProjectService } from '../../services/project.services';

@Component({
  selector: 'app-project-resource',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="page" *ngIf="projet">
      <div class="page-header">
        <div>
          <h1>Ressources du projet</h1>
          <p>{{ projet.nom }}</p>
        </div>

        <a routerLink="/projets" class="secondary-button">Retour aux projets</a>
      </div>

      <div *ngIf="messageSucces" class="alert success">{{ messageSucces }}</div>
      <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>

      <div class="card">
        <h2>Affecter des employés</h2>

        <div *ngIf="employes.length === 0">Aucun employé disponible.</div>

        <div class="checkbox-list" *ngIf="employes.length > 0">
          <label *ngFor="let employe of employes" class="checkbox-item">
            <input
              type="checkbox"
              [checked]="estSelectionne(employe.id!)"
              (change)="basculerEmploye(employe.id!)"
            />
            <span>{{ employe.nom }} — {{ employe.role }} ({{ employe.equipe }})</span>
          </label>
        </div>

        <div class="actions">
          <button type="button" class="primary-button" (click)="enregistrer()">
            Enregistrer l'affectation
          </button>
        </div>
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
      .card {
        background: #fff;
        border-radius: 24px;
        padding: 24px;
        border: 1px solid #eef2f7;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
      }
      .checkbox-list {
        display: flex;
        flex-direction: column;
        gap: 14px;
        margin-top: 16px;
      }
      .checkbox-item {
        display: flex;
        align-items: center;
        gap: 12px;
      }
      .actions {
        margin-top: 24px;
      }
      .primary-button,
      .secondary-button {
        height: 44px;
        padding: 0 18px;
        border-radius: 12px;
        font-weight: 600;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border: none;
        cursor: pointer;
      }
      .primary-button {
        background: linear-gradient(135deg, #2563eb, #1d4ed8);
        color: white;
      }
      .secondary-button {
        background: #eff6ff;
        color: #1d4ed8;
        border: 1px solid #bfdbfe;
      }
      .alert {
        padding: 14px 16px;
        border-radius: 14px;
        font-weight: 500;
      }
      .success {
        background: #ecfdf5;
        color: #047857;
        border: 1px solid #a7f3d0;
      }
      .error {
        background: #fef2f2;
        color: #b91c1c;
        border: 1px solid #fecaca;
      }
    `,
  ],
})
export class ProjectResourceComponent implements OnInit {
  private projectService = inject(ProjectService);
  private employeeService = inject(EmployeeService);
  private route = inject(ActivatedRoute);

  projet: Project | null = null;
  employes: Employee[] = [];
  employeIdsSelectionnes = new Set<number>();
  messageSucces = '';
  messageErreur = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.projectService.recupererProjetParId(id).subscribe({
      next: (projet) => {
        this.projet = projet;
        (projet.employes || []).forEach((e) => {
          if (e.id) {
            this.employeIdsSelectionnes.add(e.id);
          }
        });
      },
      error: () => {
        this.messageErreur = 'Impossible de charger le projet.';
      },
    });

    this.employeeService.chargerTousLesEmployes().subscribe({
      next: (data) => {
        this.employes = data;
      },
      error: () => {
        this.messageErreur = 'Impossible de charger les employés.';
      },
    });
  }

  estSelectionne(id: number): boolean {
    return this.employeIdsSelectionnes.has(id);
  }

  basculerEmploye(id: number): void {
    if (this.employeIdsSelectionnes.has(id)) {
      this.employeIdsSelectionnes.delete(id);
      return;
    }

    this.employeIdsSelectionnes.add(id);
  }

  enregistrer(): void {
    if (!this.projet?.id) {
      return;
    }

    const ids = Array.from(this.employeIdsSelectionnes);

    this.projectService.affecterEmployesAuProjet(this.projet.id, ids).subscribe({
      next: (projet) => {
        this.projet = projet;
        this.messageSucces = 'Ressources affectées avec succès.';
        this.messageErreur = '';
      },
      error: () => {
        this.messageErreur = 'Impossible d’enregistrer les affectations.';
      },
    });
  }
}
