import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Employee } from '../../models/employee.models';
import { Project } from '../../models/project.models';
import { Task, TaskPayload } from '../../models/task.models';
import { EmployeeService } from '../../services/employee.service';
import { ProjectService } from '../../services/project.services';
import { TaskService } from '../../services/task.services';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Projets</h1>
          <p>Liste synthétique des projets</p>
        </div>

        <a routerLink="/projets/nouveau" class="primary-button">Nouveau projet</a>
      </div>

      <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>
      <div *ngIf="messageSucces" class="alert success">{{ messageSucces }}</div>

      <div *ngIf="projectService.projets().length === 0 && !messageErreur" class="empty-state">
        <h2>Aucun projet disponible</h2>
        <p>Commence par créer ton premier projet.</p>
      </div>

      <div *ngIf="projectService.projets().length > 0" class="table-card">
        <table>
          <thead>
            <tr>
              <th>Nom du projet</th>
              <th>Date début</th>
              <th>Date fin</th>
              <th>Budget</th>
              <th>Statut</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <ng-container *ngFor="let projet of projectService.projets()">
              <tr>
                <td>
                  <button
                    type="button"
                    class="project-name"
                    (click)="basculerTaches(projet)"
                    [attr.aria-expanded]="estOuvert(projet)"
                  >
                    <span>{{ estOuvert(projet) ? 'v' : '>' }}</span>
                    {{ projet.nom }}
                  </button>
                </td>
                <td>{{ projet.dateDebut }}</td>
                <td>{{ projet.dateFin }}</td>
                <td>{{ projet.budget | number: '1.2-2' }}</td>
                <td>{{ projet.statut }}</td>
                <td class="actions">
                  <a class="text-button" [routerLink]="['/projets', projet.id, 'modifier']">Éditer</a>
                  <button type="button" class="danger-button" (click)="supprimerProjet(projet)">Supprimer</button>
                  <a class="text-button" [routerLink]="['/projets', projet.id, 'details']">Détails</a>
                </td>
              </tr>

              <tr *ngIf="estOuvert(projet)" class="task-row">
                <td colspan="6">
                  <div class="task-panel">
                    <div class="task-panel-header">
                      <h2>Tâches du projet</h2>
                      <button type="button" class="primary-button compact" (click)="ouvrirFormulaireTache(projet)">
                        Ajouter une tâche
                      </button>
                    </div>

                    <form
                      *ngIf="formulaireProjetId === projet.id"
                      [formGroup]="formulaireTache"
                      class="inline-form"
                      (ngSubmit)="creerTache(projet)"
                    >
                      <div class="grid">
                        <div class="field">
                          <label>Responsable</label>
                          <select formControlName="responsable">
                            <option value="">Sélectionner un chef de projet</option>
                            <option *ngFor="let employe of chefsProjet" [value]="employe.nom">
                              {{ employe.nom }}
                            </option>
                          </select>
                        </div>

                        <div class="field">
                          <label>Employé assigné</label>
                          <select formControlName="employeAssigneId">
                            <option [ngValue]="null">Aucun employé assigné</option>
                            <option *ngFor="let employe of projet.employes || []" [ngValue]="employe.id">
                              {{ employe.nom }} - {{ employe.role }}
                            </option>
                          </select>
                        </div>

                        <div class="field full">
                          <label>Nom / description de la tâche</label>
                          <textarea rows="3" formControlName="description"></textarea>
                        </div>

                        <div class="field">
                          <label>Deadline</label>
                          <input type="date" formControlName="dateLimite" />
                        </div>

                        <div class="field">
                          <label>Statut</label>
                          <select formControlName="statut">
                            <option *ngFor="let statut of statutsTache" [value]="statut">{{ statut }}</option>
                          </select>
                        </div>

                        <div class="field">
                          <label>Priorité</label>
                          <select formControlName="priorite">
                            <option *ngFor="let priorite of prioritesTache" [value]="priorite">{{ priorite }}</option>
                          </select>
                        </div>

                        <div class="field">
                          <label>Coût prévu</label>
                          <input type="number" min="0" formControlName="coutPrevu" />
                        </div>
                      </div>

                      <div class="form-actions">
                        <button type="button" class="text-button" (click)="fermerFormulaireTache()">Annuler</button>
                        <button type="submit" class="primary-button compact" [disabled]="enregistrementTache">
                          {{ enregistrementTache ? 'Création...' : 'Créer la tâche' }}
                        </button>
                      </div>
                    </form>

                    <div *ngIf="chargementTachesProjetId === projet.id" class="muted">
                      Chargement des tâches...
                    </div>

                    <div
                      *ngIf="chargementTachesProjetId !== projet.id && (tachesParProjet.get(projet.id!) || []).length === 0"
                      class="muted"
                    >
                      Aucune tâche pour ce projet.
                    </div>

                    <table *ngIf="(tachesParProjet.get(projet.id!) || []).length > 0" class="nested-table">
                      <thead>
                        <tr>
                          <th>Nom / description</th>
                          <th>Statut</th>
                          <th>Deadline</th>
                          <th>Coût prévu</th>
                          <th>Coût réel</th>
                          <th>Employé assigné</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr *ngFor="let tache of tachesParProjet.get(projet.id!) || []">
                          <td>{{ tache.description }}</td>
                          <td>{{ tache.statut }}</td>
                          <td>{{ tache.dateLimite }}</td>
                          <td>{{ tache.coutPrevu | number: '1.2-2' }}</td>
                          <td>{{ estCompletee(tache) ? (tache.coutReel | number: '1.2-2') : '-' }}</td>
                          <td>{{ tache.employeAssigne?.nom || 'Non assigné' }}</td>
                          <td class="actions">
                            <a class="text-button" [routerLink]="['/taches', tache.id, 'modifier']">Modifier</a>
                            <button type="button" class="danger-button" (click)="supprimerTache(projet, tache)">
                              Supprimer
                            </button>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </td>
              </tr>
            </ng-container>
          </tbody>
        </table>
      </div>
    </section>
  `,
  styles: [
    `
      .page { display: flex; flex-direction: column; gap: 24px; }
      .page-header { display: flex; justify-content: space-between; align-items: center; gap: 16px; }
      .page-header h1 { margin: 0; font-size: 28px; color: #111827; }
      .page-header p { margin: 8px 0 0; color: #6b7280; }
      .table-card, .empty-state { background: #fff; border-radius: 16px; padding: 24px; border: 1px solid #eef2f7; box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05); overflow-x: auto; }
      table { width: 100%; border-collapse: collapse; }
      th, td { padding: 14px; text-align: left; border-bottom: 1px solid #eef2f7; vertical-align: top; }
      th { font-size: 13px; text-transform: uppercase; color: #64748b; }
      .actions { display: flex; gap: 10px; flex-wrap: wrap; }
      .project-name { border: none; background: transparent; color: #1d4ed8; font-weight: 700; cursor: pointer; display: inline-flex; align-items: center; gap: 8px; padding: 0; text-align: left; }
      .task-row td { background: #f8fafc; }
      .task-panel { padding: 14px; display: flex; flex-direction: column; gap: 16px; }
      .task-panel-header { display: flex; justify-content: space-between; align-items: center; gap: 12px; }
      .task-panel-header h2 { margin: 0; font-size: 18px; }
      .nested-table th, .nested-table td { padding: 10px; font-size: 13px; }
      .inline-form { background: #fff; border: 1px solid #e2e8f0; border-radius: 14px; padding: 16px; }
      .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 14px; }
      .field { display: flex; flex-direction: column; gap: 7px; }
      .field.full { grid-column: 1 / -1; }
      label { font-weight: 600; color: #111827; }
      input, select, textarea { border: 1px solid #dbe3ef; border-radius: 12px; padding: 10px 12px; font-size: 14px; background: #fff; }
      input, select { height: 42px; }
      textarea { resize: vertical; }
      .form-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 14px; }
      .muted { color: #64748b; }
      .primary-button, .danger-button, .text-button { min-height: 38px; padding: 0 14px; border-radius: 10px; border: 1px solid transparent; cursor: pointer; font-weight: 600; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; }
      .primary-button { height: 44px; background: #2563eb; color: white; }
      .primary-button.compact { height: 38px; }
      .text-button { background: #f8fafc; color: #334155; border-color: #e2e8f0; }
      .danger-button { background: #fef2f2; color: #b91c1c; border-color: #fecaca; }
      .alert { padding: 14px 16px; border-radius: 14px; font-weight: 500; }
      .error { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }
      .success { background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; }
      @media (max-width: 768px) {
        .page-header, .task-panel-header { flex-direction: column; align-items: flex-start; }
        .grid { grid-template-columns: 1fr; }
      }
    `,
  ],
})
export class ProjectListComponent implements OnInit {
  readonly projectService = inject(ProjectService);
  private taskService = inject(TaskService);
  private employeeService = inject(EmployeeService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  messageErreur = '';
  messageSucces = '';
  projetOuvertId: number | null = null;
  formulaireProjetId: number | null = null;
  chargementTachesProjetId: number | null = null;
  enregistrementTache = false;
  tachesParProjet = new Map<number, Task[]>();
  employes: Employee[] = [];

  statutsTache = ['À_Faire', 'En_Cours', 'Complété', 'Annulé'];
  prioritesTache = ['Faible', 'Moyenne', 'Elevé'];

  formulaireTache = this.fb.group({
    responsable: ['', Validators.required],
    description: ['', [Validators.required, Validators.minLength(5)]],
    statut: ['À_Faire', Validators.required],
    priorite: ['Moyenne', Validators.required],
    dateLimite: ['', Validators.required],
    coutPrevu: [0, [Validators.required, Validators.min(0)]],
    employeAssigneId: [null as number | null],
  });

  get chefsProjet(): Employee[] {
    return this.employes.filter((employe) => employe.role.trim().toLowerCase() === 'chef de projet');
  }

  ngOnInit(): void {
    this.projectService.chargerTousLesProjets().subscribe({
      error: (error) => {
        this.messageErreur =
          `Impossible de charger les projets. ${error?.status ?? ''} ${error?.statusText ?? ''}`.trim();
      },
    });

    this.employeeService.chargerTousLesEmployes().subscribe({
      next: (employes) => {
        this.employes = employes;
      },
      error: () => {
        this.messageErreur = 'Impossible de charger les employés.';
      },
    });
  }

  estOuvert(projet: Project): boolean {
    return projet.id != null && this.projetOuvertId === projet.id;
  }

  basculerTaches(projet: Project): void {
    if (!projet.id) {
      return;
    }

    if (this.projetOuvertId === projet.id) {
      this.projetOuvertId = null;
      this.formulaireProjetId = null;
      return;
    }

    this.projetOuvertId = projet.id;
    this.chargerTachesProjet(projet.id);
  }

  ouvrirFormulaireTache(projet: Project): void {
    this.formulaireProjetId = projet.id ?? null;
    this.messageErreur = '';
    this.messageSucces = '';
    this.formulaireTache.reset({
      responsable: this.chefsProjet[0]?.nom ?? '',
      description: '',
      statut: 'À_Faire',
      priorite: 'Moyenne',
      dateLimite: '',
      coutPrevu: 0,
      employeAssigneId: null,
    });
  }

  fermerFormulaireTache(): void {
    this.formulaireProjetId = null;
  }

  creerTache(projet: Project): void {
    if (!projet.id || this.formulaireTache.invalid) {
      this.formulaireTache.markAllAsTouched();
      return;
    }

    const employeAssigneId = this.formulaireTache.value.employeAssigneId;
    const payload: TaskPayload = {
      projet: { id: projet.id },
      responsable: this.formulaireTache.value.responsable as string,
      description: this.formulaireTache.value.description as string,
      statut: this.formulaireTache.value.statut as string,
      priorite: this.formulaireTache.value.priorite as string,
      dateLimite: this.formulaireTache.value.dateLimite as string,
      coutPrevu: Number(this.formulaireTache.value.coutPrevu),
      coutReel: 0,
      employeAssigne: employeAssigneId ? { id: Number(employeAssigneId) } : null,
    };

    this.enregistrementTache = true;
    this.taskService.creerTache(payload).subscribe({
      next: () => {
        this.enregistrementTache = false;
        this.messageSucces = 'Tâche ajoutée au projet.';
        this.formulaireProjetId = null;
        this.projectService.chargerTousLesProjets().subscribe();
        this.chargerTachesProjet(projet.id!);
      },
      error: (error) => {
        this.enregistrementTache = false;
        this.messageErreur = error?.error?.message || "Impossible d'ajouter la tâche.";
      },
    });
  }

  supprimerTache(projet: Project, tache: Task): void {
    if (!projet.id || !tache.id || !confirm(`Supprimer la tâche "${tache.description}" ?`)) {
      return;
    }

    this.taskService.supprimerTache(tache.id).subscribe({
      next: () => {
        this.messageSucces = 'Tâche supprimée.';
        this.projectService.chargerTousLesProjets().subscribe();
        this.chargerTachesProjet(projet.id!);
      },
      error: () => {
        this.messageErreur = 'Impossible de supprimer la tâche.';
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

  estCompletee(tache: Task): boolean {
    return tache.statut?.toLowerCase().includes('compl');
  }

  private chargerTachesProjet(projetId: number): void {
    this.messageErreur = '';
    this.chargementTachesProjetId = projetId;
    this.taskService
      .recupererTachesParProjet(projetId)
      .pipe(
        finalize(() => {
          this.chargementTachesProjetId = null;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (taches) => {
          this.tachesParProjet.set(projetId, taches);
        },
        error: (error) => {
          this.tachesParProjet.set(projetId, []);
          this.messageErreur = error?.error?.message || 'Impossible de charger les tâches du projet.';
        },
      });
  }
}
