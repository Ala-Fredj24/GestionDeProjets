export interface RapportFinancierProjet {
  projetId: number;
  nomProjet: string;
  statutProjet: string;
  budgetProjet: number;
  coutPrevuTotal: number;
  coutReelTotal: number;
  ecartPrevuReel: number;
  resteBudget: number;
  tauxConsommation: number;
  depasseBudget: boolean;
  nombreTaches: number;
}