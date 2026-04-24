import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProjectService } from './project.services';
import { Project, ProjectPayload } from '../models/project.models';
import { environment } from '../../environments/environment';

describe('ProjectService', () => {
  let service: ProjectService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProjectService]
    });

    service = TestBed.inject(ProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('recupererTousLesProjets', () => {
    it('should fetch all projects', () => {
      const mockProjects: Project[] = [
        { id: 1, nom: 'Project 1', dateDebut: '2024-01-01', dateFin: '2024-12-31', budget: 10000, statut: 'En_Cours' } as Project,
        { id: 2, nom: 'Project 2', dateDebut: '2024-02-01', dateFin: '2024-11-30', budget: 15000, statut: 'Programmé' } as Project
      ];

      service.recupererTousLesProjets().subscribe((projects: Project[]) => {
        expect(projects.length).toBe(2);
        expect(projects[0].nom).toBe('Project 1');
      });

      const req = httpMock.expectOne(`${environment.apiBaseUrl}/projets`);
      expect(req.request.method).toBe('GET');
      req.flush(mockProjects);
    });
  });

  describe('recupererProjetParId', () => {
    it('should fetch a project by ID', () => {
      const mockProject: Project = {
        id: 1,
        nom: 'Project 1',
        dateDebut: '2024-01-01',
        dateFin: '2024-12-31',
        budget: 10000,
        statut: 'En_Cours'
      } as Project;

      service.recupererProjetParId(1).subscribe((project: Project) => {
        expect(project.id).toBe(1);
        expect(project.nom).toBe('Project 1');
      });

      const req = httpMock.expectOne(`${environment.apiBaseUrl}/projets/1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockProject);
    });
  });

  describe('creerProjet', () => {
    it('should create a new project', () => {
      const newProject: ProjectPayload = {
        nom: 'New Project',
        dateDebut: '2024-01-01',
        dateFin: '2024-12-31',
        budget: 20000
      } as ProjectPayload;

      const mockResponse: Project = { id: 3, ...newProject, statut: 'Programmé' } as Project;

      service.creerProjet(newProject).subscribe((project: Project) => {
        expect(project.id).toBe(3);
        expect(project.nom).toBe('New Project');
      });

      const req = httpMock.expectOne(`${environment.apiBaseUrl}/projets`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newProject);
      req.flush(mockResponse);
    });
  });

  describe('mettreAJourProjet', () => {
    it('should update an existing project', () => {
      const updateData: ProjectPayload = {
        nom: 'Updated Project',
        dateDebut: '2024-02-01',
        dateFin: '2024-11-30',
        budget: 25000
      } as ProjectPayload;

      const mockResponse: Project = { id: 1, ...updateData, statut: 'En_Cours' } as Project;

      service.mettreAJourProjet(1, updateData).subscribe((project: Project) => {
        expect(project.nom).toBe('Updated Project');
      });

      const req = httpMock.expectOne(`${environment.apiBaseUrl}/projets/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updateData);
      req.flush(mockResponse);
    });
  });

  describe('supprimerProjet', () => {
    it('should delete a project', () => {
      service.supprimerProjet(1).subscribe(() => {
        expect(true).toBe(true);
      });

      const req = httpMock.expectOne(`${environment.apiBaseUrl}/projets/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });
});

