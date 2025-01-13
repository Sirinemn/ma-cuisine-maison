import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { User } from '../interface/user';
import { MessageResponse } from '../interface/api/messageResponse.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  afterEach(() => { 
    httpMock.verify(); 
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('should fetch user by ID', () => {
    const dummyUser: User = { id: 123, pseudo: 'John Doe', email: 'johndoe@mail.com', firstname: 'John', lastname: 'Doe', dateOfBirth: '01/02/2000',roles: ['USER'] };

    service.getUserById('123').subscribe(user => {
      expect(user).toEqual(dummyUser);
    });

    const req = httpMock.expectOne(`${service['pathService']}/123`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyUser);
  });
  it('should update user profile', () => {
    const formData: FormData = new FormData();
    const response: MessageResponse = { message: 'Profile updated successfully' };

    service.updateProfile(formData, '123').subscribe(res => {
      expect(res.message).toBe('Profile updated successfully');
    });

    const req = httpMock.expectOne(`${service['pathService']}/123`);
    expect(req.request.method).toBe('PUT');
    req.flush(response);
  });
  it('should delete user account', () => {
    service.deleteAccount('123').subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${service['pathService']}/123`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});

