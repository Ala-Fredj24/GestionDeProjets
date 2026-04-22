import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Project, ProjectPayload } from '../models/project.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/projets`;
  readonly projets = signal<Project[]>([]);
  readonly totalProjets = computed(() => this.projets().length);
  readonly budgetTotal = computed(() =>
    this.projets().reduce((somme, projet) => somme + Number(projet.budget || 0), 0)
  );

  recupererTousLesProjets(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  chargerTousLesProjets(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl).pipe(
      tap((projets) => this.projets.set(projets))
    );
  }

  recupererProjetParId(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }

  creerProjet(payload: ProjectPayload): Observable<Project> { 
    return this.http.post<Project>(this.apiUrl, payload);
  }

  mettreAJourProjet(id: number, payload: ProjectPayload): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${id}`, payload);
  }

  supprimerProjet(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
