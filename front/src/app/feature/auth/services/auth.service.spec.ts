import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { RegisterRequest } from '../interfaces/register-request';
import { LoginRequest } from '../interfaces/login-request';
import { SessionInformation } from '../../../interface/session-information';
import { User } from '../../../interface/user';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(),
        AuthService
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  afterEach(() => {
    httpMock.verify();
  });
  it('should register a user', () => {
    const mockRegisterRequest: RegisterRequest = { email: 'test@mail.com', password: 'password', firstname: 'first', lastname: 'last', dateOfBirth: '', pseudo: 'pseudo' };
    
    service.register(mockRegisterRequest).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`${service['pathService']}/register`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should login a user', () => {
    const mockLoginRequest: LoginRequest = { email: 'test@mail.com', password: 'password' };
    const mockSessionInfo: SessionInformation = { token: 'dummy-token', userId: 1, roles: ['USER'], pseudo: 'pseudo' };
    
    service.login(mockLoginRequest).subscribe(response => {
      expect(response).toEqual(mockSessionInfo);
    });

    const req = httpMock.expectOne(`${service['pathService']}/authentication`);
    expect(req.request.method).toBe('POST');
    req.flush(mockSessionInfo);
  });

  it('should confirm account activation', () => {
    const token = 'activation-token';
    
    service.confirm(token).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`${service['pathService']}/activate-account?token=${token}`);
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('should return current user data', () => {
    const mockUser: User = { id: 1, email: 'test@mail.com', pseudo: 'testuser', roles: ['USER'], dateOfBirth: '', firstname: 'first', lastname: 'last' };
    
    service.me().subscribe(response => {
      expect(response).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`${service['pathService']}/me`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should handle 401 error in me() method', () => {
    service.me().subscribe(
      () => fail('should have failed with 401 error'),
      error => {
        expect(error.message).toContain('Erreur inconnue');
      }
    );

    const req = httpMock.expectOne(`${service['pathService']}/me`);
    expect(req.request.method).toBe('GET');
    req.flush({ message: 'Unauthorized' }, { status: 401, statusText: 'Unauthorized' });
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
