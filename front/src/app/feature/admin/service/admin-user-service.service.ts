import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/interface/user';

@Injectable({
  providedIn: 'root'
})
export class AdminUserServiceService {

  private pathService = "http://localhost:8080/api/admin/users";

  constructor(private httpClient: HttpClient) {}

  public getUserById(id: string): Observable<User>{
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }

  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.pathService);
  }

  updateUser(id: number, user: User): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/${id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.pathService}/${id}`);
  }

}
