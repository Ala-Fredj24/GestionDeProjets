import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProjectListComponent } from './pages/projects/project-list.component';
import { TaskListComponent } from './pages/tasks/task-list.component';
import { ProjectFormComponent } from './pages/projects/project-form.component';
import { ProjectResourceComponent } from './pages/projects/project-resource.component';
import { ProjectTeamComponent } from './pages/projects/project-team.component';
import { ProjectDetailComponent } from './pages/projects/project-detail.component';
import { TaskFormComponent } from './pages/tasks/task-form.component';
import { FinancialReportComponent } from './pages/reports/financial-report.component';
import { EmployeeListComponent } from './pages/employees/employee-list.component';
import { EmployeeFormComponent } from './pages/employees/employee-form.component';
import { RessourceListComponent } from './pages/ressources/ressource-list.component';
import { RessourceFormComponent } from './pages/ressources/ressource-form.component';
import { LoginComponent } from './pages/auth/login.component';
import { ChefDashboardComponent } from './pages/dashboard/chef-dashboard.component';
import { AccessDeniedComponent } from './pages/shared/access-denied.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },

  {
    path: 'admin/dashboard',
    component: DashboardComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'chef/dashboard',
    component: ChefDashboardComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['CHEF_PROJET'] },
  },
  {
    path: 'projets',
    component: ProjectListComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'projets/nouveau',
    component: ProjectFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'projets/:id/modifier',
    component: ProjectFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'projets/:id/details',
    component: ProjectDetailComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'projets/:id/ressources',
    component: ProjectResourceComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'projets/:id/equipe',
    component: ProjectTeamComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'projets/:id/taches',
    component: TaskListComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'taches',
    component: TaskListComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'taches/nouveau',
    component: TaskFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'taches/:id/modifier',
    component: TaskFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'rapports-financiers',
    component: FinancialReportComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'employes',
    component: EmployeeListComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'employes/nouveau',
    component: EmployeeFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'employes/:id/modifier',
    component: EmployeeFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'ressources',
    component: RessourceListComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'ressources/nouveau',
    component: RessourceFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'ressources/:id/modifier',
    component: RessourceFormComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
  },

  {
    path: 'acces-refuse',
    component: AccessDeniedComponent,
    canActivate: [authGuard],
  },

  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' },
];
