import { Component, EventEmitter, HostListener, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select'
import { MatListModule } from '@angular/material/list';
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
    NgIf,
    MatListModule,
    RouterModule,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  categories: string[] = ['ENTREES', 'PLATS_PRINCIPAUX', 'ACCOMPAGNEMENTS', 'DESSERTS', 'BOISSONS', 'PETITS_DEJEUNERS_BRUNCHS', 'CUISINE_DU_MONDE'];
  public user: User | undefined;
  public userRole: string[] = [];
  public isAdmin: boolean = false;
  public OnRecipeListPage: boolean = false;
  public menuOpen = false;
  public isSmallScreen = false;
  @Output() categorySelected = new EventEmitter<string | null>();

  constructor(private sessionService: SessionService,
    private router: Router
  ) { 
  }

  ngOnInit(): void {
    if (!this.sessionService.user) {
    }
    this.user = this.sessionService.user;
    if (this.user) {
      this.userRole = this.user.roles || [];
      this.isAdmin = this.userRole.includes('ADMIN');
    }
    this.router.events.subscribe(() => {
      this.OnRecipeListPage = this.router.url.startsWith('/recipe/list');
    })
  }
  onCategoryChange(category: string | null): void { 
    this.categorySelected.emit(category); 
}
  navigateToMe() {
    this.router.navigate(['/me']);
  }
  navigateToUserList() {
    this.router.navigate(['admin/users-list']);
  }
  @HostListener('window:resize', ['$event'])
  checkScreenSize() {
    this.isSmallScreen = window.innerWidth <= 600;
  }
  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }
}
