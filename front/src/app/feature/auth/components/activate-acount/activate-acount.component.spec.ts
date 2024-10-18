import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateAcountComponent } from './activate-acount.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('ActivateAcountComponent', () => {
  let component: ActivateAcountComponent;
  let fixture: ComponentFixture<ActivateAcountComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {

    TestBed.configureTestingModule({
    imports: [ActivateAcountComponent],
    providers: [
      AuthService,
      provideHttpClient(), // Nouvelle API pour les clients HTTP
      provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
    ]
});
    fixture = TestBed.createComponent(ActivateAcountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should acticate account with success', () => {
    const token = 'valid_token';
    const mockResponse = of<void>(void 0); // Simuler un Observable de succès

    // Mock du service `confirm` pour retourner un succès
    jest.spyOn(authService, 'confirm').mockReturnValue(mockResponse);

    component.onCodeCompleted(token);

    expect(authService.confirm).toHaveBeenCalledWith(token);
    expect(component.isOkay).toBe(true);
    expect(component.message).toBe('Your account has been successfully activated.\n Now you can proceed to login');
    expect(component.submitted).toBe(true);
  });
  it('should handle error when token is invalid or expired', () => {
    const token = 'invalid_token';
    const mockErrorResponse = throwError(() => new Error('Invalid token'));

    // Mock du service `confirm` pour retourner une erreur
    jest.spyOn(authService, 'confirm').mockReturnValue(mockErrorResponse);

    component.onCodeCompleted(token);

    expect(authService.confirm).toHaveBeenCalledWith(token);
    expect(component.isOkay).toBe(false);
    expect(component.message).toBe('Token has been expired or invalid');
    expect(component.submitted).toBe(true);
  });

  it('should redirect to login page', () => {
    jest.spyOn(router, "navigate");
    component.redirectToLogin();
    expect(router.navigate).toHaveBeenCalledWith(['auth/login']);
  });
});
