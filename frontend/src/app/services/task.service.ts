import { inject, Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { Task } from "../models/task.models";
import { Observable } from "rxjs/internal/Observable";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class TaskService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiBaseUrl}/tasks`
    getAll():Observable<Task[]> {
        return this.http.get<Task[]>(this.apiUrl);
    }   

}