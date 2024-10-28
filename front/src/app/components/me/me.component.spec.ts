import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideAnimations } from '@angular/platform-browser/animations';

import { MeComponent } from './me.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from '../../service/session.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { UserService } from '../../service/user.service';
import { provideRouter } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: Partial<SessionService>;
  let mockUserService: Partial<UserService>;
  let mockDialog: Partial<MatDialog>;
  let mockSnackBar: Partial<MatSnackBar>;


  beforeEach(async () => {
    mockSessionService = {
      user: {
        id: 1,
        email: 'test@mail.com',
        pseudo: 'testuser',
        roles: ['USER'],
        firstname: 'John',
        lastname: 'Doe',
        dateOfBirth: '2000-01-01'
      },
      logOut: jest.fn() 
    };
    mockUserService = {
      updateProfile: jest.fn().mockReturnValue(of({ message: 'Profile updated successfully!' })),
      deleteAccount: jest.fn().mockReturnValue(of({}))
    };

    mockDialog = {
      open: jest.fn().mockReturnValue({
        afterClosed: () => of(true)
      })
    };

    mockSnackBar = {
      open: jest.fn(),  // Mock explicitement la méthode open de MatSnackBar
    } as unknown as MatSnackBar;

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatSnackBarModule,
        MatDialogModule,
        MeComponent
      ],
      providers: [
        provideRouter([]),
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(),
        provideAnimations(),
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatDialog, useValue: mockDialog },
        { provide: MatSnackBar, useValue: mockSnackBar },

      ],
    }).compileComponents();
    

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    mockSnackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should initialize form with user data', () => {
    const expectedFormValue = {
      email: 'test@mail.com',
      pseudo: 'testuser',
      firstname: 'John',
      lastname: 'Doe'
    };
    component.ngOnInit();
    expect(component.profileForm.getRawValue()).toEqual(expectedFormValue);
  });

  it('should update user profile', () => {
    // Initialise les valeurs requises pour le formulaire pour garantir qu'il est valide
    component.profileForm.setValue({
      email: 'test@mail.com',
      pseudo: 'updatedUser',
      firstname: 'Jane',
      lastname: 'Doe',
    });

    // Appelle la méthode
    component.onUpdate();

    // Vérifie si updateProfile a été appelé avec les bons paramètres
    expect(mockUserService.updateProfile).toHaveBeenCalledWith(expect.any(FormData), '1');

    // Vérifie si snackBar.open a été appelé avec le message de succès
    expect(mockSnackBar.open).toHaveBeenCalledWith('Profile updated successfully!', 'OK', { duration: 3000 });
  });  

  it('should log out the user and navigate to login', () => {
    const navigateSpy = jest.spyOn(component['router'], 'navigate');
    component.onLogout();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['auth/login']);
  });
  

  it('should delete the user account', () => {
    component.onDelete();
    expect(mockDialog.open).toHaveBeenCalled();
    expect(mockUserService.deleteAccount).toHaveBeenCalled();
  });

});
