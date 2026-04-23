import { Employee } from './employee.models';
import { Project } from './project.models';

export interface Task {
  id?: number;
  projet?: Project;
  responsable: string;
  description: string;
  statut: string;
  priorite: string;
  dateLimite: string;
  coutReel: number;
  coutPrevu: number;
  employeAssigne?: Employee|null;
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
  coutReel: number;
  coutPrevu: number;
  employeAssigne?:{ id: number }|null;
}