import type { Employee } from './employee.models';
import type { ProjectRessource } from './ressource.models';
import type { Task } from './task.models';

export interface Project {
  id?: number;
  nom: string;
  dateDebut: string;
  dateFin: string;
  budget: number;
  statut: string;
  employes?: Employee[];
}
export interface ProjectPayload {
  nom: string;
  dateDebut: string;
  dateFin: string;
  budget: number;
  statut: string;
}

export interface ProjectDetails {
  projet: Project;
  taches: Task[];
  ressources: ProjectRessource[];
  employes: Employee[];
  coutTachesPrevu: number;
  coutTachesReel: number;
  coutRessources: number;
  coutGlobal: number;
}
