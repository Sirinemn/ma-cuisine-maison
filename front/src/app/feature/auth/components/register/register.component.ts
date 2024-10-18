import { Component, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../interfaces/register-request';
import { DatePipe, formatDate, NgIf } from '@angular/common';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss'],
    standalone: true,
    imports: [MatButtonModule, MatIconModule, MatCardModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatDatepickerModule, NgIf, MatNativeDateModule ],
    providers: [ MatDatepickerModule]
})
export class RegisterComponent implements OnDestroy{
  
  public errorMessage: string = "";
  public httpSubscription!: Subscription;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],  
    lastname: [  '', [Validators.required, Validators.min(4), Validators.max(20)], ],
    firstname: [  '', [Validators.required, Validators.min(4), Validators.max(20)], ],
    pseudo: [  '', [Validators.required, Validators.min(4), Validators.max(20)], ],
    dateOfBirth:  ['',[Validators.required],],
    password: [ '', [Validators.required, Validators.min(4), Validators.max(40)],],
  });

  public submit(): void {

    const registerRequest = this.form.value as RegisterRequest;

    this.httpSubscription = this.authService.register(registerRequest).subscribe(
      () => { 
        this.router.navigate(['auth/activate-account'])
      }, 
      (error )=> {
        this.errorMessage= error.error.error;
    });
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
