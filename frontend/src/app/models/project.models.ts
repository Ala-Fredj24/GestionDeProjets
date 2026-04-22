export interface Project {
  id?: number;
  nom: string;
  dateDebut: string;
  dateFin: string;
  budget: number;
  statut: string;
}
export interface ProjectPayload {
  nom: string;
  dateDebut: string;
  dateFin: string;
  budget: number;
  statut: string;
}