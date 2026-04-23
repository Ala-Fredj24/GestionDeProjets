import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Project } from '../../models/project.models';
import { Task, TaskPayload } from '../../models/task.models';
import { ProjectService } from '../../services/project.services';
import { TaskService } from '../../services/task.services';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.models';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>{{ modeEdition ? 'Modifier la tâche' : 'Nouvelle tâche' }}</h1>
          <p>
            {{
              modeEdition
                ? 'Mettre à jour une tâche existante'
                : 'Créer une tâche liée à un projet existant'
            }}
          </p>
        </div>

        <a routerLink="/taches" class="secondary-button">Retour à la liste</a>
      </div>

      <div *ngIf="projets.length === 0" class="alert info">
        Aucun projet disponible. Crée d'abord un projet avant d'ajouter une tâche.
      </div>

      <div *ngIf="employes.length === 0 && !messageErreur" class="alert info">
        Aucun employé disponible. Crée d'abord un employé avant d'ajouter une tâche.
      </div>

      <div class="form-card">
        <form [formGroup]="formulaire" (ngSubmit)="soumettre()">
          <div *ngIf="messageSucces" class="alert success">
            {{ messageSucces }}
          </div>

          <div *ngIf="messageErreur" class="alert error">
            {{ messageErreur }}
          </div>

          <div class="grid">
            <div class="field">
              <label for="projetId">Projet</label>
              <select id="projetId" formControlName="projetId">
                <option [ngValue]="null">Sélectionner un projet</option>
                <option *ngFor="let projet of projets" [ngValue]="projet.id">
                  {{ projet.nom }}
                </option>
              </select>
              <small *ngIf="f.projetId.touched && f.projetId.invalid">
                Le projet est obligatoire.
              </small>
            </div>

            <div class="field">
              <label for="responsable">Responsable</label>
              <input
                id="responsable"
                type="text"
                formControlName="responsable"
                placeholder="Ex: Ali"
              />
              <small *ngIf="f.responsable.touched && f.responsable.invalid">
                Le responsable est obligatoire.
              </small>
            </div>

            <div class="field full-width">
              <label for="description">Description</label>
              <textarea
                id="description"
                formControlName="description"
                rows="4"
                placeholder="Décrire la tâche..."
              ></textarea>
              <small *ngIf="f.description.touched && f.description.invalid">
                La description est obligatoire et doit contenir au moins 5 caractères.
              </small>
            </div>

            <div class="field">
              <label for="statut">Statut</label>
              <select id="statut" formControlName="statut">
                <option *ngFor="let statut of statutsTache" [value]="statut">
                  {{ statut }}
                </option>
              </select>
            </div>

            <div class="field">
              <label for="priorite">Priorité</label>
              <select id="priorite" formControlName="priorite">
                <option *ngFor="let priorite of prioritesTache" [value]="priorite">
                  {{ priorite }}
                </option>
              </select>
            </div>

            <div class="field">
              <label for="dateLimite">Date limite</label>
              <input id="dateLimite" type="date" formControlName="dateLimite" />
              <small *ngIf="f.dateLimite.touched && f.dateLimite.invalid">
                La date limite est obligatoire.
              </small>
            </div>
          </div>
          <div class="field">
            <label for="coutPrevu">Coût prévu</label>
            <input
              id="coutPrevu"
              type="number"
              formControlName="coutPrevu"
              placeholder="Ex: 3000"
            />
            <small *ngIf="f.coutPrevu.touched && f.coutPrevu.invalid">
              Le coût prévu est obligatoire et doit être positif ou nul.
            </small>
          </div>
          <div class="field">
            <label for="coutReel">Coût réel</label>
            <input id="coutReel" type="number" formControlName="coutReel" placeholder="Ex: 2500" />
            <small *ngIf="f.coutReel.touched && f.coutReel.invalid">
              Le coût réel est obligatoire et doit être positif ou nul.
            </small>
          </div>
          <div class="field">
            <label for="employeAssigneId">Employé assigné</label>
            <select id="employeAssigneId" formControlName="employeAssigneId">
              <option [ngValue]="null">Aucun employé assigné</option>
              <option *ngFor="let employe of employes" [ngValue]="employe.id">
                {{ employe.nom }} — {{ employe.role }}
              </option>
            </select>
          </div>
          <div class="actions">
            <button type="button" class="secondary-button" routerLink="/taches">Annuler</button>

            <button
              type="submit"
              class="primary-button"
              [disabled]="enregistrementEnCours || projets.length === 0"
            >
              {{
                enregistrementEnCours
                  ? 'Enregistrement...'
                  : modeEdition
                    ? 'Mettre à jour la tâche'
                    : 'Créer la tâche'
              }}
            </button>
          </div>
        </form>
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

      .form-card {
        background: #ffffff;
        border-radius: 24px;
        padding: 28px;
        border: 1px solid #eef2f7;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
      }

      .grid {
        display: grid;
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 20px;
      }

      .field {
        display: flex;
        flex-direction: column;
        gap: 8px;
      }

      .full-width {
        grid-column: 1 / -1;
      }

      label {
        font-weight: 600;
        color: #111827;
      }

      input,
      select,
      textarea {
        border: 1px solid #dbe3ef;
        border-radius: 14px;
        padding: 12px 14px;
        font-size: 15px;
        background: #ffffff;
        color: #111827;
        outline: none;
        resize: vertical;
      }

      input,
      select {
        height: 48px;
      }

      input:focus,
      select:focus,
      textarea:focus {
        border-color: #2563eb;
        box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
      }

      small {
        color: #dc2626;
        font-size: 13px;
      }

      .actions {
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        margin-top: 28px;
      }

      .primary-button,
      .secondary-button {
        height: 44px;
        padding: 0 18px;
        border-radius: 12px;
        border: none;
        cursor: pointer;
        font-weight: 600;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
      }

      .primary-button {
        background: linear-gradient(135deg, #2563eb, #1d4ed8);
        color: white;
      }

      .primary-button:disabled {
        opacity: 0.7;
        cursor: not-allowed;
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

      .info {
        background: #eff6ff;
        color: #1d4ed8;
        border: 1px solid #bfdbfe;
      }

      .success {
        background: #ecfdf5;
        color: #047857;
        border: 1px solid #a7f3d0;
        margin-bottom: 20px;
      }

      .error {
        background: #fef2f2;
        color: #b91c1c;
        border: 1px solid #fecaca;
        margin-bottom: 20px;
      }

      @media (max-width: 768px) {
        .grid {
          grid-template-columns: 1fr;
        }

        .page-header {
          flex-direction: column;
          align-items: flex-start;
        }

        .actions {
          flex-direction: column;
        }
      }
    `,
  ],
})
export class TaskFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private projectService = inject(ProjectService);
  private taskService = inject(TaskService);
  private employeeService = inject(EmployeeService);
  statutsTache = ['À_Faire', 'En_Cours', 'Complété', 'Annulé'];
  prioritesTache = ['Faible', 'Moyenne', 'Elevé'];
  modeEdition = false;
  tacheId: number | null = null;
  enregistrementEnCours = false;
  messageSucces = '';
  messageErreur = '';

  formulaire = this.fb.group({
    projetId: [null as number | null, Validators.required],
    responsable: ['', [Validators.required, Validators.minLength(2)]],
    description: ['', [Validators.required, Validators.minLength(5)]],
    statut: ['À_Faire', Validators.required],
    priorite: ['Moyenne', Validators.required],
    dateLimite: ['', Validators.required],
    coutReel: [0, [Validators.required, Validators.min(0)]],
    coutPrevu: [0, [Validators.required, Validators.min(0)]],
    employeAssigneId: [null as number | null],
  });

  get f() {
    return this.formulaire.controls;
  }

  get projets(): Project[] {
    return this.projectService.projets();
  }

  get employes(): Employee[] {
    return this.employeeService.employes();
  }

  ngOnInit(): void {
    const chargerEmployes = () => {
      if (this.employeeService.employes().length === 0) {
        this.employeeService.chargerTousLesEmployes().subscribe({
          error: () => {
            this.messageErreur = 'Impossible de charger la liste des employés.';
          },
        });
      }
    };

    if (this.projectService.projets().length > 0) {
      this.chargerTacheSiNecessaire();
      chargerEmployes();
      return;
    }

    this.projectService.chargerTousLesProjets().subscribe({
      next: () => {
        this.chargerTacheSiNecessaire();
        chargerEmployes();
      },
      error: () => {
        this.messageErreur = 'Impossible de charger la liste des projets.';
      },
    });
  }

  soumettre(): void {
    this.messageSucces = '';
    this.messageErreur = '';

    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const projetId = Number(this.formulaire.value.projetId);
    const employeAssigneId = this.formulaire.value.employeAssigneId;

    const payload: TaskPayload = {
      projet: {
        id: projetId,
      },
      responsable: this.formulaire.value.responsable as string,
      description: this.formulaire.value.description as string,
      statut: this.formulaire.value.statut as string,
      priorite: this.formulaire.value.priorite as string,
      dateLimite: this.formulaire.value.dateLimite as string,
      coutReel: Number(this.formulaire.value.coutReel),
      coutPrevu: Number(this.formulaire.value.coutPrevu),
      employeAssigne: employeAssigneId ? { id: Number(employeAssigneId) } : null,
    };

    this.enregistrementEnCours = true;

    const operation =
      this.modeEdition && this.tacheId !== null
        ? this.taskService.mettreAJourTache(this.tacheId, payload)
        : this.taskService.creerTache(payload);

    operation.subscribe({
      next: () => {
        this.enregistrementEnCours = false;
        this.messageSucces = this.modeEdition
          ? 'La tâche a été mise à jour avec succès.'
          : 'La tâche a été créée avec succès.';

        this.taskService.chargerToutesLesTaches().subscribe();

        setTimeout(() => {
          this.router.navigateByUrl('/taches');
        }, 900);
      },
      error: (error) => {
        this.enregistrementEnCours = false;
        this.messageErreur =
          error?.error?.message ||
          error?.error?.error ||
          `Erreur lors de ${this.modeEdition ? 'la mise à jour' : 'la création'} de la tâche.`;
      },
    });
  }

  private chargerTacheSiNecessaire(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      return;
    }

    const id = Number(idParam);
    if (Number.isNaN(id)) {
      this.messageErreur = 'Identifiant de tâche invalide.';
      return;
    }

    this.tacheId = id;
    this.modeEdition = true;

    this.taskService.recupererTacheParId(this.tacheId).subscribe({
      next: (tache: Task) => {
        this.formulaire.patchValue({
          projetId: tache.projet?.id ?? null,
          responsable: tache.responsable,
          description: tache.description,
          statut: tache.statut,
          priorite: tache.priorite,
          dateLimite: tache.dateLimite,
          employeAssigneId: tache.employeAssigne?.id ?? null,
        });
      },
      error: (error) => {
        this.messageErreur =
          error?.error?.message ||
          error?.error?.error ||
          'Impossible de charger la tâche à modifier.';
      },
    });
  }
}
