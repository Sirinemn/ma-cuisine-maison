import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interface/user';
import { MessageResponse } from '../interface/api/messageResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService = 'http://localhost:8080/api/user';
  
  constructor(private httpClient: HttpClient) {}

  public getUserById(id: string): Observable<User>{
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }
  public updateProfile(form: FormData,id: string): Observable<MessageResponse> {
    return this.httpClient.put<MessageResponse>(
      `${this.pathService}/${id}`,form);
  }
  public deleteAccount(id: string): Observable<any> {
    return this.httpClient.delete(`${this.pathService}/${id}`);
  }  
}
