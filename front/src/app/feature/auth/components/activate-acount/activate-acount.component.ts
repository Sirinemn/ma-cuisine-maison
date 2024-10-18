import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CodeInputModule } from 'angular-code-input';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-activate-acount',
    templateUrl: './activate-acount.component.html',
    styleUrls: ['./activate-acount.component.scss'],
    standalone: true,
    imports: [NgIf, CodeInputModule]
})
export class ActivateAcountComponent {

   message = '';
   isOkay = true;
   submitted = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  private confirmAccount(token: string) {
    console.log(token);
    this.authService.confirm(
      token
    ).subscribe({
      next: () => {
        this.isOkay = true;
        this.message = 'Your account has been successfully activated.\n Now you can proceed to login';
        this.submitted = true;
      },
      error: () => {
        this.message = 'Token has been expired or invalid';
        this.submitted = true;
        this.isOkay = false;
      }
    });
  }

  redirectToLogin() {
    this.router.navigate(['auth/login']);
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }


}
