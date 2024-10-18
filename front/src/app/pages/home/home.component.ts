import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
    imports: [MatButtonModule, MatIconModule]
})
export class HomeComponent {

  constructor(private router: Router){}

  
  connect() {
    this.router.navigate(['auth/login']);
  }
  register() {
    this.router.navigate(['auth/register']);
  }

}
