import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Task, TaskPayload } from '../models/task.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/taches`;
  readonly taches = signal<Task[]>([]);
  readonly totalTaches = computed(() => this.taches().length);

  recupererToutesLesTaches(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  chargerToutesLesTaches(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl).pipe(
      tap((taches) => this.taches.set(taches))
    );
  }

  recupererTacheParId(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  creerTache(payload: TaskPayload): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, payload);
  }

  mettreAJourTache(id: number, payload: TaskPayload): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, payload);
  }

  supprimerTache(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
