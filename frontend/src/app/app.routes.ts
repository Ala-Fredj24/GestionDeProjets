import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ProjectListComponent } from './pages/projects/project-list.component';
import { TaskListComponent } from './pages/tasks/task-list.component';
import { ProjectFormComponent } from './pages/projects/project-form.component';
import { TaskFormComponent } from './pages/tasks/task-form.component';
import { FinancialReportComponent } from './pages/reports/financial-report.component';
import { EmployeeListComponent } from './pages/employees/employee-list.component';
import { EmployeeFormComponent } from './pages/employees/employee-form.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'projets', component: ProjectListComponent },
  { path: 'projets/nouveau', component: ProjectFormComponent },
  { path: 'projets/:id/modifier', component: ProjectFormComponent },
  { path: 'taches', component: TaskListComponent },
  { path: 'taches/nouveau', component: TaskFormComponent },
  { path: 'taches/:id/modifier', component: TaskFormComponent },
  { path: 'rapports-financiers', component: FinancialReportComponent },
  { path: 'employes', component: EmployeeListComponent },
  { path: 'employes/nouveau', component: EmployeeFormComponent },
  { path: 'employes/:id/modifier', component: EmployeeFormComponent },
  //{ path: 'projets/:id/ressources', component: ProjectResourcesComponent },
  { path: '**', redirectTo: '' }
];
