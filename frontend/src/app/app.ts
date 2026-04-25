// import { CommonModule } from '@angular/common';
// import { Component, inject } from '@angular/core';
// import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
// import { AuthService } from './services/auth.service';

// @Component({
//   selector: 'app-root',
//   imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
//   templateUrl: './app.html',
//   styleUrl: './app.css'
// })
// export class App {
//   readonly authService = inject(AuthService);
//   readonly router = inject(Router);

//   estPageConnexion(): boolean {
//     return this.router.url === '/login';
//   }

//   estAdmin(): boolean {
//     return this.authService.aRole('ADMIN');
//   }

//   estChefProjet(): boolean {
//     return this.authService.aRole('CHEF_PROJET');
//   }

//   deconnexion(): void {
//     this.authService.deconnexion();
//   }
// }
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  readonly authService = inject(AuthService);
  readonly router = inject(Router);

  projetsOpen = true;
  tachesOpen = true;

  estPageConnexion(): boolean {
    return this.router.url === '/login';
  }

  estAdmin(): boolean {
    return this.authService.aRole('ADMIN');
  }

  estChefProjet(): boolean {
    return this.authService.aRole('CHEF_PROJET');
  }

  toggleProjets(): void {
    this.projetsOpen = !this.projetsOpen;
  }

  toggleTaches(): void {
    this.tachesOpen = !this.tachesOpen;
  }

  deconnexion(): void {
    this.authService.deconnexion();
  }
}
