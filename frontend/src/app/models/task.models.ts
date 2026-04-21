import { Project } from "./project.models";
export interface Task {
    id?: number;
    responsable: string;
    description: string;
    statut: string;
    priorite: string;
    projet?: Project;
    dateLimite: string;
}