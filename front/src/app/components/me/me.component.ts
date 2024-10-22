import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SessionService } from '../../service/session.service';
import { UserService } from '../../service/user.service';


@Component({
  selector: 'app-me',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss'
})
export class MeComponent implements OnInit{
    profileForm: FormGroup;
  
    constructor(
      private fb: FormBuilder,
      private sessionService: SessionService,
      private router: Router,
      private snackBar: MatSnackBar,
      private userService: UserService
    ) {
      this.profileForm = this.fb.group({
        email: [{value: '', disabled: true}, [Validators.required, Validators.email]],
        pseudo: ['', [Validators.required, Validators.minLength(3)]],
        firstname: ['', Validators.required],
        lastname: ['', Validators.required],
        dateOfBirth: ['', Validators.required]
      });
    }
  
    ngOnInit(): void {
      if (this.sessionService.user) {
        const user = this.sessionService.user!;
        this.profileForm.patchValue({
          email: user.email,
          pseudo: user.pseudo,
          firstname: user.firstname,
          lastname: user.lastname,
          dateOfBirth: user.dateOfBirth
        });
      } else {
        console.error('User is undefined in sessionService.');
      }
    }
  
    onUpdate(): void {
      if (this.profileForm.valid) {
        const userId = String(this.sessionService.user!.id); 
        this.userService.updateProfile(this.profileForm.value, userId).subscribe(
          response => {
            this.snackBar.open('Profil mis à jour', 'OK', { duration: 2000 });
          },
          error => {
            this.snackBar.open('Erreur de mise à jour', 'OK', { duration: 2000 });
          }
        );
      }
    }
  
    onLogout(): void {
      this.sessionService.logOut();
      this.router.navigate(['auth/login']);
    }
  
    onDelete(): void {
      const userId = String(this.sessionService.user!.id);
      this.userService.deleteAccount(userId).subscribe(
        response => {
          this.sessionService.logOut();
          this.router.navigate(['']);
        },
        error => {
          this.snackBar.open('Erreur de suppression de compte', 'OK', { duration: 2000 });
        }
      );
    
   }
 }
   
