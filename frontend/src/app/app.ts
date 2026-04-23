import { Component } from '@angular/core';
import { NgIf } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgIf],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'Gestion de projet';

  projetsOpen = true;
  tachesOpen = false;

  toggleProjets(): void {
    this.projetsOpen = !this.projetsOpen;
  }

  toggleTaches(): void {
    this.tachesOpen = !this.tachesOpen;
  }
}