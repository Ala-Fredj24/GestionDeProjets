export interface Project {
    id?: number;
    nom: string;
    description: string;
    dateDebut: Date;
    dateFin: Date;
    statut: string;
    budget: number;
}