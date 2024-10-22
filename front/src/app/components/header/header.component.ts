import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select'
import { User } from '../../interface/user';
import { SessionService } from '../../service/session.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    NgIf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  categories: string[] = ['Desserts', 'Plats principaux', 'Entrées', 'Boissons'];
  public user: User | undefined;
  public userRole: string[] = [];
  public isAdmin: boolean = false;

  constructor(private sessionService: SessionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (!this.sessionService.user) {
      this.sessionService.retrieveUser(); // Assurer que retrieveUser est appelé
    }
    this.user = this.sessionService.user;
    if (this.user) {
      this.userRole = this.user.roles || [];
      this.isAdmin = this.userRole.includes('ADMIN');
    }
  }
  navigateToMe() {
    this.router.navigate(['/me']);
  }
}
