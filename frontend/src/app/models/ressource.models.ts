export interface Ressource {
  id?: number;
  nom: string;
  type: string;
  cout: number;
  disponibilite: boolean;
}

export interface RessourcePayload {
  nom: string;
  type: string;
  cout: number;
  disponibilite: boolean;
}

export interface ProjectRessource {
  id?: number;
  projetId: number;
  ressourceId: number;
  nomRessource: string;
  typeRessource: string;
  disponibilite: boolean;
  quantite: number;
  coutUnitaire: number;
  coutTotal: number;
  note?: string | null;
}

export interface ProjectRessourcePayload {
  ressourceId: number;
  quantite: number;
  coutUnitaire: number;
  note?: string | null;
}
