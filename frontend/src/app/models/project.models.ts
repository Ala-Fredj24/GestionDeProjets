import { Employee } from "./employee.models";

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