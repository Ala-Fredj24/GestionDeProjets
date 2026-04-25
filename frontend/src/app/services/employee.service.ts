import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Employee, EmployeePayload } from '../models/employee.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/employes`;
  readonly employes = signal<Employee[]>([]);
  readonly totalEmployes = computed(() => this.employes().length);

  chargerTousLesEmployes(): Observable<Employee[]> {
    return this.http
      .get<Employee[]>(this.apiUrl)
      .pipe(tap((employes) => this.employes.set(employes)));
  }

  chargerEmployeParId(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  creerEmploye(payload: EmployeePayload): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl, payload);
  }

  mettreAJourEmploye(id: number, payload: EmployeePayload): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/${id}`, payload);
  }

  supprimerEmploye(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
