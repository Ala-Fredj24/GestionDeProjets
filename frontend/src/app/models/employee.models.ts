export interface Employee {
  id?: number;
  nom: string;
  email: string;
  role: string;
  equipe: string;
}

export interface EmployeePayload {
  nom: string;
  email: string;
  role: string;
  equipe: string;
}
