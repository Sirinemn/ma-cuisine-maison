import { Component, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminUserService } from '../../service/admin-user.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar'
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MessageResponse } from '../../../../interface/api/messageResponse.interface';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
@Component({
  selector: 'app-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule 
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.scss'
})
export class FormComponent implements OnDestroy{
  userForm: FormGroup;
  private httpSubscriptions: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private adminService: AdminUserService,
    private snackBar: MatSnackBar
  ) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      pseudo: ['', [Validators.required, Validators.minLength(3)]],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.httpSubscriptions.push(this.adminService.getUserById(id).subscribe(user => {
      this.userForm.patchValue(user);
    }));
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const userId = this.route.snapshot.paramMap.get('id')!;
      const formData = new FormData();
      formData.append('pseudo', this.userForm!.get('pseudo')?.value);
      formData.append('firstname', this.userForm!.get('firstname')?.value);
      formData.append('lastname', this.userForm!.get('lastname')?.value);
      this.httpSubscriptions.push(this.adminService.updateUser(formData, userId).subscribe(
        (messageResponse: MessageResponse) => {
          this.snackBar.open(messageResponse.message, 'OK', { duration: 3000 });
        },
        error => {
          this.snackBar.open('Erreur de mise à jour', 'OK', { duration: 3000 });
        }
      ));
    }
  }  
  public back() {
    window.history.back();
  }
  ngOnDestroy(): void {
    this.httpSubscriptions.forEach(subscribtion=> subscribtion.unsubscribe());
  } 
}
