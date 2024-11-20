import { Injectable } from '@angular/core';
import { User } from '../interface/user';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from '../feature/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public isLogged = false;
  public user: User | undefined;
  public userRole!:string[];

  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  constructor( private authService: AuthService) {
  }
  
  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
    
  }

  public logIn(user: User): void {
    this.user = user;
    this.isLogged = true;
    this.userRole = user.roles;
    this.next();
  }

  public logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
  
}
