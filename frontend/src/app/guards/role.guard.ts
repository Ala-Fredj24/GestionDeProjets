import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { UserRole } from '../models/auth.models';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const rolesAutorises = (route.data?.['roles'] ?? []) as UserRole[];

  if (rolesAutorises.length === 0) {
    return true;
  }

  if (authService.aRole(...rolesAutorises)) {
    return true;
  }

  return router.createUrlTree(['/acces-refuse']);
};
