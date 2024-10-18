import { CanActivate, Router } from '@angular/router';
import { SessionService } from '../service/session.service';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UnauthGuard implements CanActivate {
  constructor(private router: Router, private sessionService: SessionService) {}

  public canActivate(): boolean {
    if (this.sessionService.isLogged) {
      this.router.navigate(['reception/welcome']);
      return false;
    }
    return true;
  }
}
