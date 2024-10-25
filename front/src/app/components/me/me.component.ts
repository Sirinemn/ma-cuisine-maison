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
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { provideAnimations } from '@angular/platform-browser/animations';
import { MessageResponse } from '../../interface/api/messageResponse.interface';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';


@Component({
  selector: 'app-me',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule 
  ],
  providers: [ MatDatepickerModule,
    provideAnimations()
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
      private userService: UserService,
      private dialog: MatDialog
    ) {
      this.profileForm = this.fb.group({
        email: [{value: '', disabled: true}, [Validators.required, Validators.email]],
        pseudo: ['', [Validators.required, Validators.minLength(3)]],
        firstname: ['', Validators.required],
        lastname: ['', Validators.required],
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
        });
      } else {
        console.error('User is undefined in sessionService.');
      }
    }
  
    onUpdate(): void {
      if (this.profileForm.valid) {
        const pseudo = this.profileForm.get('pseudo')?.value;
        const firstname = this.profileForm.get('firstname')?.value;
        const lastname = this.profileForm.get('lastname')?.value;
    
        if (pseudo && firstname && lastname) {
          const userId = String(this.sessionService.user!.id);
          const formData = new FormData();
          formData.append('pseudo', pseudo);
          formData.append('firstname', firstname);
          formData.append('lastname', lastname);
          
          this.userService.updateProfile(formData, userId).subscribe(
            (messageResponse: MessageResponse) => {
              this.snackBar.open(messageResponse.message, 'OK', { duration: 3000 });
            },
            error => {
              this.snackBar.open('Erreur de mise à jour', 'OK', { duration: 3000 });
            }
          );
        }
      }
    }    
  
    onLogout(): void {
      this.sessionService.logOut();
      this.router.navigate(['auth/login']);
    }
  
    onDelete(): void {
      const dialogRef = this.dialog.open(ConfirmDialogComponent);

      const userId = String(this.sessionService.user!.id);
      dialogRef.afterClosed().subscribe(() => {
        this.userService.deleteAccount(userId).subscribe(
          response => {
            this.sessionService.logOut();
            this.router.navigate(['']);
          },
          error => {
            this.snackBar.open('Erreur de suppression de compte', 'OK', { duration: 2000 });
          }
        );
      })
      
    }    
 }
   

