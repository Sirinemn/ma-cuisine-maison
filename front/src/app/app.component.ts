import { Component } from '@angular/core';
import { SessionService } from './service/session.service';
import { Observable } from 'rxjs';
import { User } from './interface/user';
import { AuthService } from './feature/auth/services/auth.service';
import { RouterLinkActive, RouterLink, RouterOutlet } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { NgIf, AsyncPipe } from '@angular/common';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: true,
    imports: [
        NgIf,
        MatIconModule,
        MatSidenavModule,
        RouterLinkActive,
        RouterLink,
        RouterOutlet,
        AsyncPipe,
    ],
})
export class AppComponent {
  title = 'Mon starter';
  public user!:User;

  constructor(
    private authService: AuthService,
    private sessionService: SessionService
  ) {}

  public ngOnInit(): void {
    this.autoLog();
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public autoLog(): void {
    this.authService.me().subscribe({
    next:  (user: User) => {
        this.sessionService.logIn(user);
        this.user=user;
      },
     error: (err) => {
      if (err.status === 401) {
        console.log('Utilisateur non authentifié, déconnexion automatique.');
        this.sessionService.logOut();
      } else {
        console.error('Erreur lors de la récupération des informations utilisateur :', err);
      }
      }
  });
  }
}
