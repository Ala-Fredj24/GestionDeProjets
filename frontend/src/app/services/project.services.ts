import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Project, ProjectDetails, ProjectPayload } from '../models/project.models';
import { environment } from '../../environments/environment';
import { RapportFinancierProjet } from '../models/rapport-financier.models';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/projets`;
  private rapportApiUrl = `${environment.apiBaseUrl}/rapports-financiers/projets`;
  readonly projets = signal<Project[]>([]);
  readonly totalProjets = computed(() => this.projets().length);
  readonly budgetTotal = computed(() =>
    this.projets().reduce((somme, projet) => somme + Number(projet.budget || 0), 0),
  );

  recupererTousLesProjets(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  chargerTousLesProjets(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl).pipe(tap((projets) => this.projets.set(projets)));
  }

  recupererProjetParId(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }

  recupererDetailsProjet(id: number): Observable<ProjectDetails> {
    return this.http.get<ProjectDetails>(`${this.apiUrl}/${id}/details`);
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
  chargerTousLesRapportsFinanciers(): Observable<RapportFinancierProjet[]> {
    return this.http.get<RapportFinancierProjet[]>(this.rapportApiUrl);
  }

  chargerRapportFinancierParProjet(projetId: number): Observable<RapportFinancierProjet> {
    return this.http.get<RapportFinancierProjet>(`${this.rapportApiUrl}/${projetId}`);
  }
  affecterEmployesAuProjet(projetId: number, employeIds: number[]): Observable<Project> {
    return this.http.put<Project>(`${this.apiUrl}/${projetId}/employes`, employeIds);
  }
  chargerMesProjetsChef(): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.apiBaseUrl}/chef/projets`);
  }
}
