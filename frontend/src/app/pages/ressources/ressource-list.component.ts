import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { catchError, finalize, forkJoin, map, of, switchMap } from 'rxjs';
import { Project } from '../../models/project.models';
import { ProjectRessource, RessourcePayload } from '../../models/ressource.models';
import { ProjectService } from '../../services/project.services';
import { RessourceService } from '../../services/ressource.service';

@Component({
  selector: 'app-ressource-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>Ressources par projet</h1>
          <p>Ressources demandees, couts et actions par projet.</p>
        </div>
      </div>

      <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>
      <div *ngIf="messageSucces" class="alert success">{{ messageSucces }}</div>

      <div *ngIf="chargement" class="card">Chargement des ressources...</div>

      <div *ngIf="!chargement && projets.length === 0" class="empty-state">
        Aucun projet disponible.
      </div>

      <div *ngIf="!chargement && projets.length > 0 && totalRessourcesGlobal === 0" class="empty-state">
        Aucune ressource disponible.
      </div>

      <div *ngFor="let projet of projets" class="project-card">
        <div class="project-header">
          <div>
            <h2>{{ projet.nom }}</h2>
            <p>Budget: {{ projet.budget | number: '1.2-2' }}</p>
          </div>
          <div class="header-actions">
            <div class="total">Total ressources: {{ totalProjet(projet) | number: '1.2-2' }}</div>
            <button type="button" class="primary-button" (click)="ouvrirFormulaire(projet)">
              Ajouter ressource
            </button>
          </div>
        </div>

        <form
          *ngIf="formulaireProjetId === projet.id"
          [formGroup]="formulaire"
          class="inline-form"
          (ngSubmit)="enregistrer(projet)"
        >
          <div class="grid">
            <div class="field">
              <label>Nom de la ressource</label>
              <input type="text" formControlName="nom" />
            </div>

            <div class="field">
              <label>Type</label>
              <select formControlName="typeSelection" (change)="gererTypeSelection()">
                <option *ngFor="let type of typesRessource" [value]="type">{{ type }}</option>
                <option value="Autres">Autres</option>
              </select>
            </div>

            <div class="field" *ngIf="typeAutresSelectionne">
              <label>Preciser le type de ressource</label>
              <input type="text" formControlName="typePersonnalise" />
              <small *ngIf="formulaire.controls.typePersonnalise.touched && formulaire.controls.typePersonnalise.invalid">
                Le type personnalise est obligatoire.
              </small>
            </div>

            <div class="field">
              <label>Quantite</label>
              <input type="number" min="1" formControlName="quantite" />
            </div>

            <div class="field">
              <label>Cout unitaire</label>
              <input type="number" min="0" formControlName="coutUnitaire" />
            </div>

            <div class="field">
              <label>Description / note</label>
              <input type="text" formControlName="note" />
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="text-button" (click)="fermerFormulaire()">Annuler</button>
            <button type="submit" class="primary-button" [disabled]="enregistrement">
              {{ enregistrement ? 'Enregistrement...' : affectationEnEdition ? 'Modifier' : 'Ajouter' }}
            </button>
          </div>
        </form>

        <div *ngIf="ressourcesProjet(projet).length === 0" class="empty-inline">
          Aucune ressource demandee pour ce projet.
        </div>

        <table *ngIf="ressourcesProjet(projet).length > 0">
          <thead>
            <tr>
              <th>Nom ressource</th>
              <th>Type</th>
              <th>Quantite</th>
              <th>Cout unitaire</th>
              <th>Cout total</th>
              <th>Description</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let affectation of ressourcesProjet(projet)">
              <td>{{ affectation.nomRessource }}</td>
              <td>{{ affectation.typeRessource }}</td>
              <td>{{ affectation.quantite }}</td>
              <td>{{ affectation.coutUnitaire | number: '1.2-2' }}</td>
              <td>{{ affectation.coutTotal | number: '1.2-2' }}</td>
              <td>{{ affectation.note || '-' }}</td>
              <td class="actions">
                <button type="button" class="text-button" (click)="modifier(projet, affectation)">Modifier</button>
                <button type="button" class="danger-button" (click)="supprimer(projet, affectation)">Supprimer</button>
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
      .page-header h1 { margin: 0; color: #111827; }
      .page-header p { margin: 8px 0 0; color: #64748b; }
      .card, .project-card, .empty-state { background: #fff; border-radius: 16px; padding: 22px; border: 1px solid #eef2f7; box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05); }
      .project-card { display: flex; flex-direction: column; gap: 16px; overflow-x: auto; }
      .project-header { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
      .project-header h2 { margin: 0; font-size: 20px; }
      .project-header p { margin: 6px 0 0; color: #64748b; }
      .header-actions { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; justify-content: flex-end; }
      .total { font-weight: 700; color: #111827; }
      table { width: 100%; border-collapse: collapse; }
      th, td { padding: 13px; text-align: left; border-bottom: 1px solid #eef2f7; }
      th { font-size: 12px; text-transform: uppercase; color: #64748b; }
      .actions, .form-actions { display: flex; gap: 10px; flex-wrap: wrap; }
      .form-actions { justify-content: flex-end; margin-top: 14px; }
      .inline-form { border: 1px solid #e2e8f0; border-radius: 14px; padding: 16px; background: #f8fafc; }
      .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 14px; }
      .field { display: flex; flex-direction: column; gap: 7px; }
      label { font-weight: 600; color: #111827; }
      input, select { height: 42px; border: 1px solid #dbe3ef; border-radius: 12px; padding: 0 12px; background: #fff; }
      small { color: #dc2626; font-size: 12px; }
      .primary-button, .danger-button, .text-button { min-height: 38px; padding: 0 14px; border-radius: 10px; border: 1px solid transparent; cursor: pointer; font-weight: 600; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; }
      .primary-button { background: #2563eb; color: #fff; }
      .primary-button:disabled { opacity: 0.7; cursor: not-allowed; }
      .text-button { background: #fff; color: #334155; border-color: #e2e8f0; }
      .danger-button { background: #fef2f2; color: #b91c1c; border-color: #fecaca; }
      .empty-inline { color: #64748b; padding: 10px 0; }
      .alert { padding: 14px 16px; border-radius: 14px; font-weight: 500; }
      .error { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }
      .success { background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; }
      @media (max-width: 768px) {
        .project-header { flex-direction: column; }
        .header-actions { justify-content: flex-start; }
        .grid { grid-template-columns: 1fr; }
      }
    `,
  ],
})
export class RessourceListComponent implements OnInit {
  private fb = inject(FormBuilder);
  private projectService = inject(ProjectService);
  private ressourceService = inject(RessourceService);
  private cdr = inject(ChangeDetectorRef);

  readonly typesRessource = ['Materielle', 'Financiere', 'Logicielle'];

  projets: Project[] = [];
  ressourcesParProjet = new Map<number, ProjectRessource[]>();
  formulaireProjetId: number | null = null;
  affectationEnEdition: ProjectRessource | null = null;
  chargement = true;
  enregistrement = false;
  messageErreur = '';
  messageSucces = '';

  formulaire = this.fb.group({
    nom: ['', Validators.required],
    typeSelection: ['Materielle', Validators.required],
    typePersonnalise: [''],
    quantite: [1, [Validators.required, Validators.min(1)]],
    coutUnitaire: [0, [Validators.required, Validators.min(0)]],
    note: [''],
  });

  get typeAutresSelectionne(): boolean {
    return this.formulaire.value.typeSelection === 'Autres';
  }

  ngOnInit(): void {
    this.chargerPage();
  }

  ressourcesProjet(projet: Project): ProjectRessource[] {
    return projet.id ? this.ressourcesParProjet.get(projet.id) || [] : [];
  }

  totalProjet(projet: Project): number {
    return this.ressourcesProjet(projet).reduce((total, item) => total + Number(item.coutTotal || 0), 0);
  }

  get totalRessourcesGlobal(): number {
    return Array.from(this.ressourcesParProjet.values()).reduce(
      (total, ressources) => total + ressources.length,
      0,
    );
  }

  ouvrirFormulaire(projet: Project): void {
    this.messageErreur = '';
    this.messageSucces = '';
    this.formulaireProjetId = projet.id ?? null;
    this.affectationEnEdition = null;
    this.formulaire.reset({
      nom: '',
      typeSelection: 'Materielle',
      typePersonnalise: '',
      quantite: 1,
      coutUnitaire: 0,
      note: '',
    });
    this.gererTypeSelection();
  }

  fermerFormulaire(): void {
    this.formulaireProjetId = null;
    this.affectationEnEdition = null;
  }

  gererTypeSelection(initialiserMontant = true): void {
    const typePersonnalise = this.formulaire.controls.typePersonnalise;
    if (this.typeAutresSelectionne) {
      typePersonnalise.setValidators([Validators.required, Validators.minLength(2)]);
      if (initialiserMontant) {
        this.formulaire.patchValue({ coutUnitaire: 0 }, { emitEvent: false });
      }
    } else {
      typePersonnalise.clearValidators();
      typePersonnalise.setValue('', { emitEvent: false });
    }
    typePersonnalise.updateValueAndValidity({ emitEvent: false });
  }

  enregistrer(projet: Project): void {
    this.messageErreur = '';
    this.messageSucces = '';
    this.gererTypeSelection(false);

    if (!projet.id || this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const type = this.typeAutresSelectionne
      ? (this.formulaire.value.typePersonnalise || '').trim()
      : (this.formulaire.value.typeSelection || '').trim();

    const coutUnitaire = Number(this.formulaire.value.coutUnitaire || 0);
    const ressourcePayload: RessourcePayload = {
      nom: (this.formulaire.value.nom || '').trim(),
      type,
      cout: coutUnitaire,
      disponibilite: true,
    };
    const quantite = Number(this.formulaire.value.quantite || 1);
    const note = this.formulaire.value.note || null;

    const operation = this.affectationEnEdition?.id
      ? this.ressourceService.mettreAJourRessource(this.affectationEnEdition.ressourceId, ressourcePayload).pipe(
          switchMap((ressource) =>
            this.ressourceService.modifierRessourceProjet(projet.id!, this.affectationEnEdition!.id!, {
              ressourceId: ressource.id!,
              quantite,
              coutUnitaire,
              note,
            }),
          ),
        )
      : this.ressourceService.creerRessource(ressourcePayload).pipe(
          switchMap((ressource) =>
            this.ressourceService.ajouterRessourceAuProjet(projet.id!, {
              ressourceId: ressource.id!,
              quantite,
              coutUnitaire,
              note,
            }),
          ),
        );

    this.enregistrement = true;
    operation.pipe(finalize(() => {
      this.enregistrement = false;
      this.cdr.detectChanges();
    })).subscribe({
      next: () => {
        this.messageSucces = this.affectationEnEdition ? 'Ressource modifiee.' : 'Ressource ajoutee au projet.';
        this.fermerFormulaire();
        this.rafraichirProjet(projet.id!);
      },
      error: (error) => {
        this.messageErreur = error?.error?.message || "Impossible d'enregistrer la ressource.";
      },
    });
  }

  modifier(projet: Project, affectation: ProjectRessource): void {
    this.messageErreur = '';
    this.messageSucces = '';
    const typeStandard = this.typesRessource.includes(affectation.typeRessource);

    this.formulaireProjetId = projet.id ?? null;
    this.affectationEnEdition = affectation;
    this.formulaire.patchValue({
      nom: affectation.nomRessource,
      typeSelection: typeStandard ? affectation.typeRessource : 'Autres',
      typePersonnalise: typeStandard ? '' : affectation.typeRessource,
      quantite: affectation.quantite,
      coutUnitaire: Number(affectation.coutUnitaire),
      note: affectation.note || '',
    });
    this.gererTypeSelection(false);
  }

  supprimer(projet: Project, affectation: ProjectRessource): void {
    if (!projet.id || !affectation.id || !confirm(`Supprimer ${affectation.nomRessource} du projet ?`)) {
      return;
    }

    this.messageErreur = '';
    this.messageSucces = '';
    this.ressourceService.supprimerRessourceProjet(projet.id, affectation.id).subscribe({
      next: () => {
        this.messageSucces = 'Ressource supprimee du projet.';
        this.rafraichirProjet(projet.id!);
      },
      error: () => {
        this.messageErreur = 'Impossible de supprimer cette ressource.';
      },
    });
  }

  private chargerPage(): void {
    this.chargement = true;
    this.messageErreur = '';
    this.ressourcesParProjet.clear();
    this.projectService.chargerTousLesProjets()
      .pipe(
        switchMap((projets) => {
          this.projets = [...projets];
          const projetsAvecId = projets.filter((projet) => projet.id != null);
          if (projetsAvecId.length === 0) {
            return of([] as Array<{ projetId: number; ressources: ProjectRessource[] }>);
          }

          return forkJoin(
            projetsAvecId.map((projet) =>
              this.ressourceService.chargerRessourcesProjet(projet.id!).pipe(
                map((ressources) => ({ projetId: projet.id!, ressources })),
                catchError(() => {
                  this.messageErreur = 'Certaines ressources de projet n ont pas pu etre chargees.';
                  return of({ projetId: projet.id!, ressources: [] as ProjectRessource[] });
                }),
              ),
            ),
          );
        }),
        finalize(() => {
          this.chargement = false;
          this.cdr.detectChanges();
        }),
      )
      .subscribe({
      next: (resultats) => {
        this.ressourcesParProjet = new Map(
          resultats.map((resultat) => [resultat.projetId, resultat.ressources]),
        );
      },
      error: () => {
        this.messageErreur = 'Impossible de charger les projets.';
        this.projets = [];
        this.ressourcesParProjet = new Map();
      },
    });
  }

  private rafraichirProjet(projetId: number): void {
    forkJoin({
      projets: this.projectService.chargerTousLesProjets(),
      ressources: this.ressourceService.chargerRessourcesProjet(projetId),
    }).subscribe({
      next: ({ projets, ressources }) => {
        this.projets = [...projets];
        this.ressourcesParProjet = new Map(this.ressourcesParProjet).set(projetId, ressources);
        this.chargement = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.messageErreur = 'La ressource a ete enregistree, mais le rafraichissement a echoue.';
      },
    });
  }
}
