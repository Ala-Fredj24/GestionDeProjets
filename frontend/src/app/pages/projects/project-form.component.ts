import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Project, ProjectPayload } from '../../models/project.models';
import { ProjectService } from '../../services/project.services';

@Component({
  selector: 'app-project-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>{{ modeEdition ? 'Modifier le projet' : 'Nouveau projet' }}</h1>
          <p>{{ modeEdition ? 'Mettre à jour le projet existant' : 'Créer un projet avec budget, dates et statut' }}</p>
        </div>

        <a routerLink="/projets" class="secondary-button">Retour à la liste</a>
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
            <div class="field full-width">
              <label for="nom">Nom du projet</label>
              <input id="nom" type="text" formControlName="nom" placeholder="Ex: Projet Gestion RH" />
              <small *ngIf="f.nom.touched && f.nom.invalid">
                Le nom est obligatoire et doit contenir au moins 3 caractères.
              </small>
            </div>

            <div class="field">
              <label for="dateDebut">Date de début</label>
              <input id="dateDebut" type="date" formControlName="dateDebut" />
              <small *ngIf="f.dateDebut.touched && f.dateDebut.invalid">
                La date de début est obligatoire.
              </small>
            </div>

            <div class="field">
              <label for="dateFin">Date de fin</label>
              <input id="dateFin" type="date" formControlName="dateFin" />
              <small *ngIf="f.dateFin.touched && f.dateFin.invalid">
                La date de fin est obligatoire.
              </small>
            </div>

            <div class="field">
              <label for="budget">Budget</label>
              <input id="budget" type="number" formControlName="budget" placeholder="Ex: 100000" />
              <small *ngIf="f.budget.touched && f.budget.invalid">
                Le budget est obligatoire et doit être supérieur à 0.
              </small>
            </div>

            <div class="field">
              <label for="statut">Statut</label>
              <select id="statut" formControlName="statut">
                <option *ngFor="let statut of statutsProjet" [value]="statut">
                  {{ statut }}
                </option>
              </select>
            </div>
          </div>

          <div class="actions">
            <button type="button" class="secondary-button" routerLink="/projets">
              Annuler
            </button>

            <button type="submit" class="primary-button" [disabled]="enregistrementEnCours">
              {{ enregistrementEnCours ? 'Enregistrement...' : (modeEdition ? 'Mettre à jour le projet' : 'Créer le projet') }}
            </button>
          </div>
        </form>
      </div>
    </section>
  `,
  styles: [`
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
    select {
      height: 48px;
      border: 1px solid #dbe3ef;
      border-radius: 14px;
      padding: 0 14px;
      font-size: 15px;
      background: #ffffff;
      color: #111827;
      outline: none;
    }

    input:focus,
    select:focus {
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
      margin-bottom: 20px;
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
  `]
})
export class ProjectFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private projectService = inject(ProjectService);

  statutsProjet = ['Programmé', 'En_Cours', 'Completé', 'Annulé'];
  modeEdition = false;
  projetId: number | null = null;
  enregistrementEnCours = false;
  messageSucces = '';
  messageErreur = '';

  formulaire = this.fb.group({
    nom: ['', [Validators.required, Validators.minLength(3)]],
    dateDebut: ['', Validators.required],
    dateFin: ['', Validators.required],
    budget: [null as number | null, [Validators.required, Validators.min(1)]],
    statut: ['Programmé', Validators.required]
  });

  get f() {
    return this.formulaire.controls;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) {
      return;
    }

    const id = Number(idParam);
    if (Number.isNaN(id)) {
      this.messageErreur = 'Identifiant de projet invalide.';
      return;
    }

    this.projetId = id;
    this.modeEdition = true;
    this.chargerProjet(this.projetId);
  }

  soumettre(): void {
    this.messageSucces = '';
    this.messageErreur = '';

    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const dateDebut = this.formulaire.value.dateDebut as string;
    const dateFin = this.formulaire.value.dateFin as string;

    if (dateDebut >= dateFin) {
      this.messageErreur = 'La date de début doit être antérieure à la date de fin.';
      return;
    }

    const payload: ProjectPayload = {
      nom: this.formulaire.value.nom as string,
      dateDebut,
      dateFin,
      budget: Number(this.formulaire.value.budget),
      statut: this.formulaire.value.statut as string
    };

    this.enregistrementEnCours = true;

    const operation = this.modeEdition && this.projetId !== null
      ? this.projectService.mettreAJourProjet(this.projetId, payload)
      : this.projectService.creerProjet(payload);

    operation.subscribe({
      next: () => {
        this.enregistrementEnCours = false;
        this.messageSucces = this.modeEdition
          ? 'Le projet a été mis à jour avec succès.'
          : 'Le projet a été créé avec succès.';

        this.projectService.chargerTousLesProjets().subscribe();

        setTimeout(() => {
          this.router.navigateByUrl('/projets');
        }, 900);
      },
      error: (error) => {
        this.enregistrementEnCours = false;
        this.messageErreur =
          error?.error?.message ||
          error?.error?.error ||
          `Erreur lors de ${this.modeEdition ? 'la mise à jour' : 'la création'} du projet.`;
      }
    });
  }

  private chargerProjet(id: number): void {
    this.projectService.recupererProjetParId(id).subscribe({
      next: (projet: Project) => {
        this.formulaire.patchValue({
          nom: projet.nom,
          dateDebut: projet.dateDebut,
          dateFin: projet.dateFin,
          budget: projet.budget ? Number(projet.budget) : null,
          statut: projet.statut
        });
      },
      error: (error) => {
        this.messageErreur =
          error?.error?.message ||
          error?.error?.error ||
          'Impossible de charger le projet à modifier.';
      }
    });
  }
}
