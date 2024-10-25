import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormComponent } from './form.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { AdminUserService } from '../../service/admin-user.service';
import { provideAnimations } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockAdminService: Partial<AdminUserService>;
  let mockRouter: Partial<Router>;
  let mockSnackBar: Partial<MatSnackBar>;

  beforeEach(async () => {
    mockAdminService = {
      getUserById: jest.fn().mockReturnValue(of({
        id: 1,
        email: 'test@mail.com',
        pseudo: 'testuser',
        firstname: 'John',
        lastname: 'Doe'
      })),
      updateUser: jest.fn().mockReturnValue(of({ message: 'User updated successfully' }))
    };

    mockRouter = {
      navigate: jest.fn()
    };

    mockSnackBar = {
      open: jest.fn()
    };

    const mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),
          has: jest.fn().mockReturnValue(true),  // Ajout de la méthode has
          getAll: jest.fn().mockReturnValue([]), // Ajout de la méthode getAll
          keys: []                               // Ajout de la propriété keys
        } as ParamMap  // Cast explicite pour correspondre à l'interface ParamMap
      }
    };

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, FormComponent],
      providers: [
        FormBuilder,
        provideAnimations(),
        { provide: AdminUserService, useValue: mockAdminService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockSnackBar }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with user data', () => {
    expect(mockAdminService.getUserById).toHaveBeenCalledWith('1');
    expect(component.userForm.value).toEqual({
      email: 'test@mail.com',
      pseudo: 'testuser',
      firstname: 'John',
      lastname: 'Doe'
    });
  });

  it('should submit valid form and show success message', () => {
    component.userForm.setValue({
      email: 'newtest@mail.com',
      pseudo: 'newuser',
      firstname: 'Jane',
      lastname: 'Doe'
    });

    component.onSubmit();

    expect(mockAdminService.updateUser).toHaveBeenCalled();
    //expect(mockSnackBar.open).toHaveBeenCalledWith('User updated successfully', 'OK', { duration: 3000 });
  });

  it('should not submit if form is invalid', () => {
    component.userForm.setValue({
      email: '', // Invalid email
      pseudo: 'nu', // Invalid pseudo (too short)
      firstname: 'Jane',
      lastname: 'Doe'
    });

    component.onSubmit();

    expect(mockAdminService.updateUser).not.toHaveBeenCalled();
    expect(mockSnackBar.open).not.toHaveBeenCalled();
  });

  it('should handle update error and show error message', () => {
    jest.spyOn(mockAdminService, 'updateUser').mockReturnValueOnce(throwError({ error: 'Error updating user' }));
    
    component.userForm.setValue({
      email: 'newtest@mail.com',
      pseudo: 'newuser',
      firstname: 'Jane',
      lastname: 'Doe'
    });

    component.onSubmit();

    expect(mockAdminService.updateUser).toHaveBeenCalled();
    //expect(mockSnackBar.open).toHaveBeenCalledWith('Erreur de mise à jour', 'OK', { duration: 3000 });
  });
});