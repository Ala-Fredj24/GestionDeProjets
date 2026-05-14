import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Project } from '../../models/project.models';
import { ProjectRessource, Ressource } from '../../models/ressource.models';
import { ProjectService } from '../../services/project.services';
import { RessourceService } from '../../services/ressource.service';

@Component({
  selector: 'app-project-resource',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="page" *ngIf="projet">
      <div class="page-header">
        <div>
          <h1>Ressources du projet</h1>
          <p>{{ projet.nom }}</p>
        </div>

        <a routerLink="/projets" class="secondary-button">Retour aux projets</a>
      </div>

      <div class="summary-grid">
        <div class="summary-card">
          <span>Budget projet</span>
          <strong>{{ projet.budget | number: '1.2-2' }}</strong>
        </div>
        <div class="summary-card">
          <span>Cout ressources</span>
          <strong>{{ totalRessources | number: '1.2-2' }}</strong>
        </div>
        <div class="summary-card" [class.warning]="budgetRestant < 0">
          <span>Budget restant apres ressources</span>
          <strong>{{ budgetRestant | number: '1.2-2' }}</strong>
        </div>
      </div>

      <div *ngIf="messageSucces" class="alert success">{{ messageSucces }}</div>
      <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>

      <div class="form-card">
        <h2>{{ affectationEnEdition ? 'Modifier une ressource affectee' : 'Ajouter une ressource' }}</h2>
        <form [formGroup]="formulaire" (ngSubmit)="enregistrer()">
          <div class="grid">
            <div class="field">
              <label for="ressourceId">Ressource</label>
              <select id="ressourceId" formControlName="ressourceId" (change)="appliquerCoutCatalogue()">
                <option [ngValue]="null">Selectionner une ressource</option>
                <option *ngFor="let ressource of ressources" [ngValue]="ressource.id">
                  {{ ressource.nom }} - {{ ressource.type }} - {{ ressource.cout | number: '1.2-2' }}
                </option>
              </select>
            </div>

            <div class="field">
              <label for="quantite">Quantite</label>
              <input id="quantite" type="number" min="1" formControlName="quantite" />
            </div>

            <div class="field">
              <label for="coutUnitaire">Cout unitaire</label>
              <input id="coutUnitaire" type="number" min="0" formControlName="coutUnitaire" />
            </div>

            <div class="field">
              <label for="note">Note</label>
              <input id="note" type="text" formControlName="note" />
            </div>
          </div>

          <div class="actions">
            <button type="button" class="secondary-button" *ngIf="affectationEnEdition" (click)="annulerEdition()">
              Annuler
            </button>
            <button type="submit" class="primary-button">
              {{ affectationEnEdition ? 'Mettre a jour' : 'Ajouter' }}
            </button>
          </div>
        </form>
      </div>

      <div class="table-card">
        <h2>Ressources affectees</h2>

        <div *ngIf="affectations.length === 0" class="empty-state">
          Aucune ressource materielle ou financiere affectee a ce projet.
        </div>

        <table *ngIf="affectations.length > 0">
          <thead>
            <tr>
              <th>Ressource</th>
              <th>Type</th>
              <th>Quantite</th>
              <th>Cout unitaire</th>
              <th>Total</th>
              <th>Disponibilite</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let affectation of affectations">
              <td>{{ affectation.nomRessource }}</td>
              <td>{{ affectation.typeRessource }}</td>
              <td>{{ affectation.quantite }}</td>
              <td>{{ affectation.coutUnitaire | number: '1.2-2' }}</td>
              <td>{{ affectation.coutTotal | number: '1.2-2' }}</td>
              <td>{{ affectation.disponibilite ? 'Disponible' : 'Indisponible' }}</td>
              <td class="row-actions">
                <button type="button" class="secondary-button small" (click)="modifier(affectation)">Modifier</button>
                <button type="button" class="danger-button small" (click)="supprimer(affectation)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  `,
  styles: [
    `
      .page { display: flex; flex-direction: column; gap: 24px; }
      .page-header { display: flex; justify-content: space-between; align-items: center; gap: 16px; }
      .summary-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 16px; }
      .summary-card, .form-card, .table-card { background: #fff; border-radius: 16px; padding: 20px; border: 1px solid #eef2f7; box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05); }
      .summary-card span { display: block; color: #64748b; margin-bottom: 8px; }
      .summary-card strong { font-size: 24px; color: #111827; }
      .summary-card.warning strong { color: #b91c1c; }
      .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 16px; }
      .field { display: flex; flex-direction: column; gap: 8px; }
      input, select { height: 44px; border: 1px solid #dbe3ef; border-radius: 12px; padding: 0 12px; }
      table { width: 100%; border-collapse: collapse; }
      th, td { padding: 14px; border-bottom: 1px solid #eef2f7; text-align: left; }
      th { font-size: 13px; text-transform: uppercase; color: #64748b; }
      .actions, .row-actions { display: flex; gap: 10px; flex-wrap: wrap; justify-content: flex-end; margin-top: 18px; }
      .row-actions { justify-content: flex-start; margin-top: 0; }
      .primary-button, .secondary-button, .danger-button { height: 42px; padding: 0 16px; border-radius: 12px; border: 1px solid transparent; font-weight: 600; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; }
      .small { height: 34px; padding: 0 12px; }
      .primary-button { background: #2563eb; color: #fff; }
      .secondary-button { background: #eff6ff; color: #1d4ed8; border-color: #bfdbfe; }
      .danger-button { background: #fef2f2; color: #b91c1c; border-color: #fecaca; }
      .alert { padding: 14px 16px; border-radius: 14px; font-weight: 500; }
      .success { background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; }
      .error { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }
      .empty-state { color: #64748b; }
      @media (max-width: 768px) {
        .summary-grid, .grid { grid-template-columns: 1fr; }
        .page-header { flex-direction: column; align-items: flex-start; }
        .table-card { overflow-x: auto; }
      }
    `,
  ],
})
export class ProjectResourceComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private projectService = inject(ProjectService);
  private ressourceService = inject(RessourceService);

  projet: Project | null = null;
  ressources: Ressource[] = [];
  affectations: ProjectRessource[] = [];
  affectationEnEdition: ProjectRessource | null = null;
  messageSucces = '';
  messageErreur = '';

  formulaire = this.fb.group({
    ressourceId: [null as number | null, Validators.required],
    quantite: [1, [Validators.required, Validators.min(1)]],
    coutUnitaire: [0, [Validators.required, Validators.min(0)]],
    note: [''],
  });

  get totalRessources(): number {
    return this.affectations.reduce((total, affectation) => total + Number(affectation.coutTotal || 0), 0);
  }

  get budgetRestant(): number {
    return Number(this.projet?.budget || 0) - this.totalRessources;
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.chargerProjet(id);
    this.chargerCatalogue();
    this.chargerAffectations(id);
  }

  appliquerCoutCatalogue(): void {
    const ressourceId = Number(this.formulaire.value.ressourceId);
    const ressource = this.ressources.find((item) => item.id === ressourceId);
    if (ressource && !this.affectationEnEdition) {
      this.formulaire.patchValue({ coutUnitaire: Number(ressource.cout || 0) });
    }
  }

  enregistrer(): void {
    if (!this.projet?.id || this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const payload = {
      ressourceId: Number(this.formulaire.value.ressourceId),
      quantite: Number(this.formulaire.value.quantite),
      coutUnitaire: Number(this.formulaire.value.coutUnitaire),
      note: this.formulaire.value.note || null,
    };

    const operation = this.affectationEnEdition?.id
      ? this.ressourceService.modifierRessourceProjet(this.projet.id, this.affectationEnEdition.id, payload)
      : this.ressourceService.ajouterRessourceAuProjet(this.projet.id, payload);

    operation.subscribe({
      next: () => {
        this.messageSucces = this.affectationEnEdition ? 'Ressource mise a jour.' : 'Ressource ajoutee au projet.';
        this.messageErreur = '';
        this.annulerEdition();
        this.chargerProjet(this.projet!.id!);
        this.chargerAffectations(this.projet!.id!);
      },
      error: (error) => {
        this.messageErreur = error?.error?.message || "Impossible d'enregistrer la ressource du projet.";
      },
    });
  }

  modifier(affectation: ProjectRessource): void {
    this.affectationEnEdition = affectation;
    this.formulaire.patchValue({
      ressourceId: affectation.ressourceId,
      quantite: affectation.quantite,
      coutUnitaire: Number(affectation.coutUnitaire),
      note: affectation.note || '',
    });
  }

  supprimer(affectation: ProjectRessource): void {
    if (!this.projet?.id || !affectation.id || !confirm(`Supprimer ${affectation.nomRessource} du projet ?`)) {
      return;
    }

    this.ressourceService.supprimerRessourceProjet(this.projet.id, affectation.id).subscribe({
      next: () => {
        this.messageSucces = 'Ressource retiree du projet.';
        this.chargerProjet(this.projet!.id!);
        this.chargerAffectations(this.projet!.id!);
      },
      error: () => {
        this.messageErreur = 'Impossible de supprimer cette ressource du projet.';
      },
    });
  }

  annulerEdition(): void {
    this.affectationEnEdition = null;
    this.formulaire.reset({ ressourceId: null, quantite: 1, coutUnitaire: 0, note: '' });
  }

  private chargerProjet(id: number): void {
    this.projectService.recupererProjetParId(id).subscribe({
      next: (projet) => {
        this.projet = projet;
      },
      error: () => {
        this.messageErreur = 'Impossible de charger le projet.';
      },
    });
  }

  private chargerCatalogue(): void {
    this.ressourceService.chargerToutesLesRessources().subscribe({
      next: (ressources) => {
        this.ressources = ressources;
      },
      error: () => {
        this.messageErreur = 'Impossible de charger le catalogue des ressources.';
      },
    });
  }

  private chargerAffectations(projetId: number): void {
    this.ressourceService.chargerRessourcesProjet(projetId).subscribe({
      next: (affectations) => {
        this.affectations = affectations;
      },
      error: () => {
        this.messageErreur = 'Impossible de charger les ressources du projet.';
      },
    });
  }
}
