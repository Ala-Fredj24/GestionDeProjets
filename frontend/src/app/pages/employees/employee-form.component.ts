import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EmployeePayload } from '../../models/employee.models';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="page">
      <div class="page-header">
        <div>
          <h1>{{ modeEdition ? 'Modifier employé' : 'Nouvel employé' }}</h1>
          <p>Créer ou modifier une ressource du projet</p>
        </div>

        <a routerLink="/employes" class="secondary-button">Retour à la liste</a>
      </div>

      <div class="form-card">
        <form [formGroup]="formulaire" (ngSubmit)="soumettre()">
          <div *ngIf="messageErreur" class="alert error">{{ messageErreur }}</div>

          <div class="grid">
            <div class="field">
              <label for="nom">Nom</label>
              <input id="nom" type="text" formControlName="nom" />
            </div>

            <div class="field">
              <label for="email">Email</label>
              <input id="email" type="email" formControlName="email" />
            </div>

            <div class="field">
              <label for="role">Rôle</label>
              <input id="role" type="text" formControlName="role" />
            </div>

            <div class="field">
              <label for="equipe">Équipe</label>
              <input id="equipe" type="text" formControlName="equipe" />
            </div>
          </div>

          <div class="actions">
            <button type="button" class="secondary-button" routerLink="/employes">Annuler</button>
            <button type="submit" class="primary-button">
              {{ modeEdition ? 'Mettre à jour' : 'Créer' }}
            </button>
          </div>
        </form>
      </div>
    </section>
  `,
  styles: [`
    .page { display: flex; flex-direction: column; gap: 24px; }
    .page-header { display: flex; justify-content: space-between; align-items: center; gap: 16px; }
    .form-card { background: #fff; border-radius: 24px; padding: 28px; border: 1px solid #eef2f7; box-shadow: 0 10px 30px rgba(15,23,42,0.05); }
    .grid { display: grid; grid-template-columns: repeat(2, minmax(0,1fr)); gap: 20px; }
    .field { display: flex; flex-direction: column; gap: 8px; }
    input { height: 48px; border: 1px solid #dbe3ef; border-radius: 14px; padding: 0 14px; }
    .actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; }
    .primary-button, .secondary-button { height: 44px; padding: 0 18px; border-radius: 12px; font-weight: 600; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; border: none; cursor: pointer; }
    .primary-button { background: linear-gradient(135deg,#2563eb,#1d4ed8); color: white; }
    .secondary-button { background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; }
    .alert { padding: 14px 16px; border-radius: 14px; margin-bottom: 18px; }
    .error { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }
  `]
})
export class EmployeeFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private employeeService = inject(EmployeeService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  employeId: number | null = null;
  modeEdition = false;
  messageErreur = '';

  formulaire = this.fb.group({
    nom: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    role: ['', Validators.required],
    equipe: ['', Validators.required]
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.modeEdition = true;
      this.employeId = Number(id);

      this.employeeService.chargerEmployeParId(this.employeId).subscribe({
        next: (employe) => {
          this.formulaire.patchValue({
            nom: employe.nom,
            email: employe.email,
            role: employe.role,
            equipe: employe.equipe
          });
        },
        error: () => {
          this.messageErreur = 'Impossible de charger cet employé.';
        }
      });
    }
  }

  soumettre(): void {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    const payload: EmployeePayload = {
      nom: this.formulaire.value.nom as string,
      email: this.formulaire.value.email as string,
      role: this.formulaire.value.role as string,
      equipe: this.formulaire.value.equipe as string
    };

    if (this.modeEdition && this.employeId) {
      this.employeeService.mettreAJourEmploye(this.employeId, payload).subscribe({
        next: () => this.router.navigateByUrl('/employes'),
        error: (error) => {
          this.messageErreur = error?.error?.message || 'Erreur lors de la mise à jour.';
        }
      });
      return;
    }

    this.employeeService.creerEmploye(payload).subscribe({
      next: () => this.router.navigateByUrl('/employes'),
      error: (error) => {
        this.messageErreur = error?.error?.message || 'Erreur lors de la création.';
      }
    });
  }
}