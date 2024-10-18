import { Component, OnDestroy } from '@angular/core';
import { LoginRequest } from '../../interfaces/login-request';
import { Subscription } from 'rxjs';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/service/session.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interface/session-information';
import { User } from 'src/app/interface/user';
import { NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    standalone: true,
    imports: [
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        NgIf,
    ],
})
export class LoginComponent implements OnDestroy {
  public hide = true;
  public httpSubscription!: Subscription;
  public errorMessage: string = '';

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.min(3)]],
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService,
  ) {}

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.httpSubscription = this.authService.login(loginRequest).subscribe(
      (response: SessionInformation) => {
        localStorage.setItem('token', response.token);
        this.authService.me()
          .subscribe((user: User) => {
            this.sessionService.logIn(user);
            this.router.navigate(['reception/welcome']);
          });
      },
      (error) => {
        console.log(error);
        this.errorMessage = error.error.error;
      }
    );
  }

  public back() {
    window.history.back();
  }

  ngOnDestroy(): void {
    if(this.httpSubscription){
      this.httpSubscription.unsubscribe();
    }
  }
}
