import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="login-page">
      <div class="login-card">
        <div class="login-header">
          <h1>Connexion</h1>
          <p>Accédez à la plateforme de gestion de projet</p>
        </div>

        <form [formGroup]="formulaire" (ngSubmit)="soumettre()">
          <div *ngIf="messageErreur" class="alert error">
            {{ messageErreur }}
          </div>

          <div class="field">
            <label for="email">Email</label>
            <input id="email" type="email" formControlName="email" placeholder="admin@admin.com" />
            <small *ngIf="f.email.touched && f.email.invalid">
              L'email est obligatoire et doit être valide.
            </small>
          </div>

          <div class="field">
            <label for="motDePasse">Mot de passe</label>
            <input id="motDePasse" type="password" formControlName="motDePasse" placeholder="••••••••" />
            <small *ngIf="f.motDePasse.touched && f.motDePasse.invalid">
              Le mot de passe est obligatoire.
            </small>
          </div>

          <button type="submit" class="primary-button" [disabled]="chargement">
            {{ chargement ? 'Connexion...' : 'Se connecter' }}
          </button>
        </form>

        <div class="demo-box">
          <strong>Compte admin de test</strong>
          <p>Email : admin@admin.com</p>
          <p>Mot de passe : admin123</p>
        </div>
      </div>
    </section>
  `,
  styles: [`
    .login-page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #eff6ff, #f8fafc);
      padding: 24px;
    }

    .login-card {
      width: 100%;
      max-width: 430px;
      background: #ffffff;
      border-radius: 28px;
      padding: 32px;
      box-shadow: 0 20px 50px rgba(15, 23, 42, 0.10);
      border: 1px solid #e5e7eb;
    }

    .login-header h1 {
      margin: 0;
      font-size: 32px;
      color: #111827;
    }

    .login-header p {
      margin: 10px 0 24px;
      color: #6b7280;
    }

    form {
      display: flex;
      flex-direction: column;
      gap: 18px;
    }

    .field {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    label {
      font-weight: 600;
      color: #111827;
    }

    input {
      height: 48px;
      border: 1px solid #dbe3ef;
      border-radius: 14px;
      padding: 0 14px;
      font-size: 15px;
      outline: none;
    }

    input:focus {
      border-color: #2563eb;
      box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
    }

    .primary-button {
      height: 48px;
      border: none;
      border-radius: 14px;
      background: linear-gradient(135deg, #2563eb, #1d4ed8);
      color: white;
      font-weight: 700;
      cursor: pointer;
      margin-top: 8px;
    }

    .primary-button:disabled {
      opacity: 0.7;
      cursor: not-allowed;
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

    small {
      color: #dc2626;
      font-size: 13px;
    }

    .demo-box {
      margin-top: 24px;
      padding: 16px;
      border-radius: 16px;
      background: #eff6ff;
      border: 1px solid #bfdbfe;
      color: #1d4ed8;
    }

    .demo-box p {
      margin: 6px 0 0;
    }
  `]
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);

  chargement = false;
  messageErreur = '';

  formulaire = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    motDePasse: ['', Validators.required]
  });

  get f() {
    return this.formulaire.controls;
  }

  soumettre(): void {
    this.messageErreur = '';

    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    this.chargement = true;

    this.authService.connexion({
      email: this.formulaire.value.email as string,
      motDePasse: this.formulaire.value.motDePasse as string
    }).subscribe({
      next: () => {
        this.chargement = false;
        this.authService.redirigerApresConnexion();
      },
      error: () => {
        this.chargement = false;
        this.messageErreur = 'Email ou mot de passe incorrect.';
      }
    });
  }
}