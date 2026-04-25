export type UserRole = 'ADMIN' | 'CHEF_PROJET' | 'EMPLOYE';

export interface AuthLoginRequest {
  email: string;
  motDePasse: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  role: UserRole;
  employeeId: number | null;
}

export interface AuthUser {
  email: string;
  role: UserRole;
  employeeId: number | null;
}
