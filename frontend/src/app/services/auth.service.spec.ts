import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { vi } from 'vitest';
import { AuthService } from './auth.service';
import { AuthLoginRequest, AuthResponse, AuthUser } from '../models/auth.models';
import { environment } from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let navigateByUrl: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    navigateByUrl = vi.fn();

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService, { provide: Router, useValue: { navigateByUrl } }],
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('connexion', () => {
    it('should send login request and save session', () => {
      const loginRequest: AuthLoginRequest = {
        email: 'test@example.com',
        motDePasse: 'password123',
      };

      const authResponse: AuthResponse = {
        token: 'jwt-token-123',
        email: 'test@example.com',
        role: 'ADMIN',
        employeeId: 1,
      };

      service.connexion(loginRequest).subscribe((response) => {
        expect(response).toEqual(authResponse);
        expect(service.getToken()).toBe('jwt-token-123');
      });

      const req = httpMock.expectOne(`${environment.apiBaseUrl}/auth/login`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);
      req.flush(authResponse);
    });
  });

  describe('deconnexion', () => {
    it('should clear token and user on logout', () => {
      service.deconnexion();
      expect(service.getToken()).toBeNull();
      expect(service.estConnecte()).toBe(false);
    });
  });

  describe('estConnecte', () => {
    it('should return false when no token', () => {
      expect(service.estConnecte()).toBe(false);
    });

    it('should return true when token exists', () => {
      const authResponse: AuthResponse = {
        token: 'jwt-token-123',
        email: 'test@example.com',
        role: 'ADMIN',
        employeeId: 1,
      };

      service.connexion({ email: 'test@example.com', motDePasse: 'password' }).subscribe(() => {
        expect(service.estConnecte()).toBe(true);
      });

      httpMock.expectOne(`${environment.apiBaseUrl}/auth/login`).flush(authResponse);
    });
  });

  describe('getRole', () => {
    it('should return user role', () => {
      const authResponse: AuthResponse = {
        token: 'jwt-token-123',
        email: 'test@example.com',
        role: 'CHEF_PROJET',
        employeeId: 1,
      };

      service.connexion({ email: 'test@example.com', motDePasse: 'password' }).subscribe(() => {
        expect(service.getRole()).toBe('CHEF_PROJET');
      });

      httpMock.expectOne(`${environment.apiBaseUrl}/auth/login`).flush(authResponse);
    });
  });

  describe('aRole', () => {
    it('should check if user has specific role', () => {
      const authResponse: AuthResponse = {
        token: 'jwt-token-123',
        email: 'test@example.com',
        role: 'ADMIN',
        employeeId: 1,
      };

      service.connexion({ email: 'test@example.com', motDePasse: 'password' }).subscribe(() => {
        expect(service.aRole('ADMIN')).toBe(true);
        expect(service.aRole('CHEF_PROJET')).toBe(false);
      });

      httpMock.expectOne(`${environment.apiBaseUrl}/auth/login`).flush(authResponse);
    });
  });
});
