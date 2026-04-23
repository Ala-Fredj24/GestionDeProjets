import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-access-denied',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="page">
      <div class="card">
        <h1>Accès refusé</h1>
        <p>Vous êtes connecté, mais vous n’avez pas les droits nécessaires pour accéder à cette page.</p>
      </div>
    </section>
  `,
  styles: [`
    .page {
      min-height: 60vh;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .card {
      max-width: 600px;
      background: #ffffff;
      border-radius: 24px;
      padding: 32px;
      border: 1px solid #eef2f7;
      box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
      text-align: center;
    }
  `]
})
export class AccessDeniedComponent {
}