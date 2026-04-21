import { inject, Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Project } from "../models/project.models";

@Injectable({
    providedIn: 'root'
})
export class ProjectService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiBaseUrl}/projects`;
    getAll():Observable<Project[]> {
        return this.http.get<Project[]>(this.apiUrl);
    }
}
