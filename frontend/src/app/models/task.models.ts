import { Project } from './project.models';

export interface Task {
  id?: number;
  projet?: Project;
  responsable: string;
  description: string;
  statut: string;
  priorite: string;
  dateLimite: string;
}
export interface TaskPayload {
  projet: {
    id: number;
  };
  responsable: string;
  description: string;
  statut: string;
  priorite: string;
  dateLimite: string;
}