import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProjectRessource, ProjectRessourcePayload, Ressource, RessourcePayload } from '../models/ressource.models';

@Injectable({
  providedIn: 'root',
})
export class RessourceService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/ressources`;
  readonly ressources = signal<Ressource[]>([]);
  readonly totalRessources = computed(() => this.ressources().length);

  chargerToutesLesRessources(): Observable<Ressource[]> {
    return this.http
      .get<Ressource[]>(this.apiUrl)
      .pipe(tap((ressources) => this.ressources.set(ressources)));
  }

  chargerRessourceParId(id: number): Observable<Ressource> {
    return this.http.get<Ressource>(`${this.apiUrl}/${id}`);
  }

  creerRessource(payload: RessourcePayload): Observable<Ressource> {
    return this.http.post<Ressource>(this.apiUrl, payload);
  }

  mettreAJourRessource(id: number, payload: RessourcePayload): Observable<Ressource> {
    return this.http.put<Ressource>(`${this.apiUrl}/${id}`, payload);
  }

  supprimerRessource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  chargerRessourcesProjet(projetId: number): Observable<ProjectRessource[]> {
    return this.http.get<ProjectRessource[]>(`${environment.apiBaseUrl}/projets/${projetId}/ressources`);
  }

  ajouterRessourceAuProjet(
    projetId: number,
    payload: ProjectRessourcePayload,
  ): Observable<ProjectRessource> {
    return this.http.post<ProjectRessource>(
      `${environment.apiBaseUrl}/projets/${projetId}/ressources`,
      payload,
    );
  }

  modifierRessourceProjet(
    projetId: number,
    affectationId: number,
    payload: ProjectRessourcePayload,
  ): Observable<ProjectRessource> {
    return this.http.put<ProjectRessource>(
      `${environment.apiBaseUrl}/projets/${projetId}/ressources/${affectationId}`,
      payload,
    );
  }

  supprimerRessourceProjet(projetId: number, affectationId: number): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiBaseUrl}/projets/${projetId}/ressources/${affectationId}`,
    );
  }
}
