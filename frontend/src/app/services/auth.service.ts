import { Injectable, PLATFORM_ID, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { AuthLoginRequest, AuthResponse, AuthUser, UserRole } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);

  private readonly apiUrl = `${environment.apiBaseUrl}/auth`;
  private readonly tokenKey = 'auth_token';
  private readonly userKey = 'auth_user';

  readonly utilisateur = signal<AuthUser | null>(null);
  readonly token = signal<string | null>(null);

  constructor() {
    if (this.estNavigateur()) {
      this.token.set(this.lireTokenDepuisStorage());
      this.utilisateur.set(this.lireUtilisateurDepuisStorage());
    }
  }

  connexion(payload: AuthLoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, payload).pipe(
      tap((response) => {
        this.sauvegarderSession(response);
      }),
    );
  }

  deconnexion(): void {
    const storage = this.getStorage();

    if (storage) {
      storage.removeItem(this.tokenKey);
      storage.removeItem(this.userKey);
    }

    this.token.set(null);
    this.utilisateur.set(null);
    this.router.navigateByUrl('/login');
  }

  estConnecte(): boolean {
    return !!this.token();
  }

  getToken(): string | null {
    return this.token();
  }

  getRole(): UserRole | null {
    return this.utilisateur()?.role ?? null;
  }

  aRole(...roles: UserRole[]): boolean {
    const role = this.getRole();
    return !!role && roles.includes(role);
  }

  redirigerApresConnexion(): void {
    const role = this.getRole();

    if (role === 'ADMIN') {
      this.router.navigateByUrl('/admin/dashboard');
      return;
    }

    if (role === 'CHEF_PROJET') {
      this.router.navigateByUrl('/chef/dashboard');
      return;
    }

    if (role === 'EMPLOYE') {
      this.router.navigateByUrl('/acces-refuse');
      return;
    }

    this.router.navigateByUrl('/login');
  }

  private sauvegarderSession(response: AuthResponse): void {
    const utilisateur: AuthUser = {
      email: response.email,
      role: response.role,
      employeeId: response.employeeId,
    };

    const storage = this.getStorage();

    if (storage) {
      storage.setItem(this.tokenKey, response.token);
      storage.setItem(this.userKey, JSON.stringify(utilisateur));
    }

    this.token.set(response.token);
    this.utilisateur.set(utilisateur);
  }

  private lireTokenDepuisStorage(): string | null {
    const storage = this.getStorage();

    if (!storage) {
      return null;
    }

    return storage.getItem(this.tokenKey);
  }

  private lireUtilisateurDepuisStorage(): AuthUser | null {
    const storage = this.getStorage();

    if (!storage) {
      return null;
    }

    const raw = storage.getItem(this.userKey);

    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as AuthUser;
    } catch {
      return null;
    }
  }

  private estNavigateur(): boolean {
    return isPlatformBrowser(this.platformId);
  }

  private getStorage(): Storage | null {
    if (!this.estNavigateur() || typeof localStorage === 'undefined') {
      return null;
    }

    const storage = localStorage as Partial<Storage>;

    if (
      typeof storage.getItem !== 'function' ||
      typeof storage.setItem !== 'function' ||
      typeof storage.removeItem !== 'function'
    ) {
      return null;
    }

    return localStorage;
  }
}
