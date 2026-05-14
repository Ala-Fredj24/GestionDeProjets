import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { RessourcePayload } from '../../models/ressource.models';
import { RessourceService } from '../../services/ressource.service';

@Component({
  selector: 'app-ressource-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>{{ modeEdition ? 'Modifier ressource' : 'Nouvelle ressource' }}</h1>
          <p>Creer ou modifier une ressource dediee au suivi projet.</p>
        </div>

        <a routerLink="/ressources" class="secondary-button">Retour a la liste</a>
      </div>

      <div class="form-card">
        <form [formGroup]="formulaire" (ngSubmit)="soumettre()">
          <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>

          <div class="grid">
            <div class="field">
              <label for="nom">Nom</label>
              <input id="nom" type="text" formControlName="nom" />
              <small *ngIf="f.nom.touched && f.nom.invalid">Le nom est obligatoire.</small>
            </div>

            <div class="field">
              <label for="type">Type</label>
              <select id="type" formControlName="typeSelection" (change)="gererTypeSelection()">
                <option *ngFor="let type of typesRessource" [value]="type">{{ type }}</option>
                <option value="Autres">Autres</option>
              </select>
            </div>

            <div class="field" *ngIf="typeAutresSelectionne">
              <label for="typePersonnalise">Preciser le type de ressource</label>
              <input id="typePersonnalise" type="text" formControlName="typePersonnalise" />
              <small *ngIf="formulaire.controls.typePersonnalise.touched && formulaire.controls.typePersonnalise.invalid">
                Le type personnalise est obligatoire.
              </small>
            </div>

            <div class="field">
              <label for="cout">Cout</label>
              <input id="cout" type="number" min="0" formControlName="cout" />
              <small *ngIf="f.cout.touched && f.cout.invalid">
                Le cout doit etre positif ou nul.
              </small>
            </div>

            <label class="check-field">
              <input type="checkbox" formControlName="disponibilite" />
              Disponible
            </label>
          </div>

          <div class="actions">
            <button type="button" class="secondary-button" routerLink="/ressources">Annuler</button>
            <button type="submit" class="primary-button">
              {{ modeEdition ? 'Mettre a jour' : 'Creer' }}
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
      .form-card {
        background: #fff;
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
      .field,
      .check-field {
        display: flex;
        flex-direction: column;
        gap: 8px;
      }
      .check-field {
        justify-content: center;
        flex-direction: row;
        align-items: center;
        font-weight: 600;
      }
      input,
      select {
        height: 48px;
        border: 1px solid #dbe3ef;
        border-radius: 14px;
        padding: 0 14px;
      }
      input[type='checkbox'] {
        width: 18px;
        height: 18px;
        padding: 0;
      }
      small {
        color: #dc2626;
        font-size: 13px;
      }
      .actions {
        display: flex;
        justify-content: flex-end;
        gap: 12px;
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
        margin-bottom: 18px;
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
      }
    `,
  ],
})
export class RessourceFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private ressourceService = inject(RessourceService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  ressourceId: number | null = null;
  modeEdition = false;
  messageErreur = '';
  readonly typesRessource = ['Materielle', 'Financiere', 'Logicielle'];

  formulaire = this.fb.group({
    nom: ['', [Validators.required, Validators.minLength(2)]],
    typeSelection: ['Materielle', Validators.required],
    typePersonnalise: [''],
    cout: [0, [Validators.required, Validators.min(0)]],
    disponibilite: [true, Validators.required],
  });

  get f() {
    return this.formulaire.controls;
  }

  get typeAutresSelectionne(): boolean {
    return this.formulaire.value.typeSelection === 'Autres';
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.modeEdition = true;
      this.ressourceId = Number(id);

      this.ressourceService.chargerRessourceParId(this.ressourceId).subscribe({
        next: (ressource) => {
          const typeStandard = this.typesRessource.includes(ressource.type);
          this.formulaire.patchValue({
            nom: ressource.nom,
            typeSelection: typeStandard ? ressource.type : 'Autres',
            typePersonnalise: typeStandard ? '' : ressource.type,
            cout: Number(ressource.cout ?? 0),
            disponibilite: ressource.disponibilite,
          });
          this.gererTypeSelection(false);
        },
        error: () => {
          this.messageErreur = 'Impossible de charger cette ressource.';
        },
      });
    }
  }

  soumettre(): void {
    this.gererTypeSelection(false);
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const type = this.typeAutresSelectionne
      ? (this.formulaire.value.typePersonnalise || '').trim()
      : (this.formulaire.value.typeSelection || '').trim();

    const payload: RessourcePayload = {
      nom: this.formulaire.value.nom as string,
      type,
      cout: Number(this.formulaire.value.cout),
      disponibilite: Boolean(this.formulaire.value.disponibilite),
    };

    if (this.modeEdition && this.ressourceId) {
      this.ressourceService.mettreAJourRessource(this.ressourceId, payload).subscribe({
        next: () => this.router.navigateByUrl('/ressources'),
        error: (error) => {
          this.messageErreur = error?.error?.message || 'Erreur lors de la mise a jour.';
        },
      });
      return;
    }

    this.ressourceService.creerRessource(payload).subscribe({
      next: () => this.router.navigateByUrl('/ressources'),
      error: (error) => {
        this.messageErreur = error?.error?.message || 'Erreur lors de la creation.';
      },
    });
  }

  gererTypeSelection(initialiserMontant = true): void {
    const typePersonnalise = this.formulaire.controls.typePersonnalise;
    if (this.typeAutresSelectionne) {
      typePersonnalise.setValidators([Validators.required, Validators.minLength(2)]);
      if (initialiserMontant) {
        this.formulaire.patchValue({ cout: 0 }, { emitEvent: false });
      }
    } else {
      typePersonnalise.clearValidators();
      typePersonnalise.setValue('', { emitEvent: false });
    }
    typePersonnalise.updateValueAndValidity({ emitEvent: false });
  }
}
