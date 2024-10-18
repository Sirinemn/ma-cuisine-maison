import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interface/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService = 'http://localhost:8080/api/user';
  
  constructor(private httpClient: HttpClient) {}

  public getUserById(id: string): Observable<User>{
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }
}
