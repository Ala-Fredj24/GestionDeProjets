import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Employee } from '../../models/employee.models';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Employés</h1>
          <p>Gestion des ressources humaines du projet</p>
        </div>

        <a routerLink="/employes/nouveau" class="primary-button">Nouvel employé</a>
      </div>

      <div *ngIf="messageErreur" class="alert error">
        {{ messageErreur }}
      </div>

      <div *ngIf="messageSucces" class="alert success">
        {{ messageSucces }}
      </div>

      <div *ngIf="employes.length === 0 && !messageErreur" class="empty-state">
        Aucun employé disponible.
      </div>

      <div *ngIf="employes.length > 0" class="table-card">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nom</th>
              <th>Email</th>
              <th>Rôle</th>
              <th>Équipe</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let employe of employes">
              <td>{{ employe.id }}</td>
              <td>{{ employe.nom }}</td>
              <td>{{ employe.email }}</td>
              <td>{{ employe.role }}</td>
              <td>{{ employe.equipe }}</td>
              <td class="actions-cell">
                <a
                  [routerLink]="['/employes', employe.id, 'modifier']"
                  class="icon-button secondary-button"
                  aria-label="Modifier l'employé"
                  title="Modifier"
                >
                  ✏️
                </a>
                <button
                  type="button"
                  class="icon-button danger-button"
                  (click)="supprimer(employe.id!)"
                  aria-label="Supprimer l'employé"
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
      .table-card,
      .empty-state {
        background: #fff;
        border-radius: 24px;
        padding: 24px;
        border: 1px solid #eef2f7;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
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
      .actions-cell {
        display: flex;
        gap: 10px;
      }
      .primary-button,
      .secondary-button,
      .danger-button {
        height: 40px;
        padding: 0 14px;
        border-radius: 12px;
        font-weight: 600;
        border: none;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
      }
      .icon-button {
        width: 40px;
        padding: 0;
        font-size: 16px;
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
      .danger-button {
        background: #fef2f2;
        color: #b91c1c;
        border: 1px solid #fecaca;
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
      .success {
        background: #ecfdf5;
        color: #047857;
        border: 1px solid #a7f3d0;
      }
    `,
  ],
})
export class EmployeeListComponent implements OnInit {
  private employeeService = inject(EmployeeService);

  messageErreur = '';
  messageSucces = '';

  get employes(): Employee[] {
    return this.employeeService.employes();
  }

  ngOnInit(): void {
    if (this.employeeService.employes().length > 0) {
      return;
    }

    this.employeeService.chargerTousLesEmployes().subscribe({
      error: () => {
        this.messageErreur = 'Impossible de charger les employés.';
      },
    });
  }

  supprimer(id: number): void {
    const confirmation = window.confirm('Voulez-vous vraiment supprimer cet employé ?');
    if (!confirmation) {
      return;
    }

    this.employeeService.supprimerEmploye(id).subscribe({
      next: () => {
        this.messageSucces = 'Employé supprimé avec succès.';
        this.employeeService.chargerTousLesEmployes().subscribe();
      },
      error: () => {
        this.messageErreur = 'Impossible de supprimer cet employé.';
      },
    });
  }
}
