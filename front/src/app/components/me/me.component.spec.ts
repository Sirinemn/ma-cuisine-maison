import { ComponentFixture, fakeAsync, flush, TestBed, tick } from '@angular/core/testing';
import { BrowserAnimationsModule, NoopAnimationsModule, provideAnimations } from '@angular/platform-browser/animations';

import { MeComponent } from './me.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from '../../service/session.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { UserService } from '../../service/user.service';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { of, throwError } from 'rxjs';
import { RecipeService } from '../../feature/recipe/service/recipe.service';
import { NO_ERRORS_SCHEMA } from '@angular/core';

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
        MeComponent,
        NoopAnimationsModule
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
        { provide: Router, useValue: { navigate: jest.fn() } },
        { provide: ActivatedRoute, useValue: { 
          snapshot: { paramMap: { get: jest.fn().mockReturnValue('1') } } 
        }},
        { provide: RecipeService, useValue: { getRecipeByUserId: jest.fn().mockReturnValue(of([])) }}
      ],
      schemas: [NO_ERRORS_SCHEMA] //to handle any unknown elements
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

  xit('should update user profile', () => { 
    // Arrange
    component.profileForm.patchValue({
      pseudo: 'updatedUser',
      firstname: 'Jane',
      lastname: 'Doe'
    });

    // Act
    component.onUpdate();

    // Assert
    expect(mockUserService.updateProfile).toHaveBeenCalled();
    
    // Get the arguments passed to updateProfile
    const updateProfileMock = mockUserService.updateProfile as jest.Mock;
    const [formData, userId] = updateProfileMock.mock.calls[0];
    
    // Check formData contents
    expect(formData.get('pseudo')).toBe('updatedUser');
    expect(formData.get('firstname')).toBe('Jane');
    expect(formData.get('lastname')).toBe('Doe');
    expect(userId).toBe('1');

    // Simulate observable completion
    (mockUserService.updateProfile as jest.Mock).mock.results[0].value.subscribe(() => {
      expect(mockSnackBar.open).toHaveBeenCalledWith(
        'Profile updated successfully!',
        'OK',
        { duration: 3000 }
      );
    });
  });

  xit('should show error message when update fails', () => { 
    // Arrange
    component.profileForm.patchValue({
      pseudo: 'updatedUser',
      firstname: 'Jane',
      lastname: 'Doe'
    });

    (mockUserService.updateProfile as jest.Mock).mockReturnValue(
      throwError(() => new Error('Update failed'))
    );

    // Act
    component.onUpdate();

    // Assert
    (mockUserService.updateProfile as jest.Mock).mock.results[0].value.subscribe({
      error: () => {
        expect(mockSnackBar.open).toHaveBeenCalledWith(
          'Erreur de mise à jour',
          'OK',
          { duration: 3000 }
        );
      }
    });
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
