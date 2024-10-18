import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterRequest } from '../interfaces/register-request';
import { catchError, Observable, throwError } from 'rxjs';
import { User } from 'src/app/interface/user';
import { SessionInformation } from 'src/app/interface/session-information';
import { LoginRequest } from '../interfaces/login-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathService = "http://localhost:8080/api/auth";
  
  constructor(private httpClient: HttpClient) { }

  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<SessionInformation> {
    return this.httpClient.post<SessionInformation>(`${this.pathService}/authentication`, loginRequest);
  }
  public confirm(token: string): Observable<void> {
    return this.httpClient.get<void>(`${this.pathService}/activate-account?token=${token}`);
  }

  public me(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`).pipe(
      catchError((err) => {
        if (err.status === 401) {
          // Gestion spécifique pour utilisateur non connecté
          console.log('L\'utilisateur n\'est pas connecté.');
        }
        return throwError(() => new Error(err.message || 'Erreur inconnue'));
      })
    );
  }
}
