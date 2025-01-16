import { Component } from '@angular/core';
import { SessionService } from './service/session.service';
import { Observable, of } from 'rxjs';
import { User } from './interface/user';
import { AuthService } from './feature/auth/services/auth.service';
import { RouterLinkActive, RouterLink, RouterOutlet, Router } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { NgIf, AsyncPipe } from '@angular/common';
import { HeaderComponent } from "./components/header/header.component";
import { FooterComponent } from "./components/footer/footer.component";

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
    HeaderComponent,
    FooterComponent
],
})
export class AppComponent {
  title = 'Ma Cuisine Maison';
  public selectedCategory: string | null = null;
  public isLogged$: Observable<boolean> = of(false);

  constructor(
    private authService: AuthService,
    private sessionService: SessionService,
    private router: Router
  ) {}

  public ngOnInit(): void {
    this.autoLog();
    this.isLogged$ = this.sessionService.$isLogged();

  }
  onCategorySelected(category: string | null): void { 
    this.router.navigate(['/recipe/list'], { queryParams: { category: category } }); 
  }
  
  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public autoLog(): void {
    this.authService.me().subscribe({
      next: (user: User) => {
        this.sessionService.logIn(user);
        if (this.router.url !== '/reception/welcome') {
          this.router.navigate(['/reception/welcome']);
        }
      },
      error: (err) => {
        if (err.status === 401) {
          console.log('Utilisateur non authentifié, déconnexion automatique.');
          this.sessionService.logOut();
        } else {
          console.error('Erreur lors de la récupération des informations utilisateur :', err);
        }
      },
    });
  }
  
}
