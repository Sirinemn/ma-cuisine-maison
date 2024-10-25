import { TestBed } from '@angular/core/testing';

import { AdminUserService } from './admin-user.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { User } from '../../../interface/user';
import { MessageResponse } from '../../../interface/api/messageResponse.interface';

describe('AdminUserServiceService', () => {
  let service: AdminUserService;
  let httpMock: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
        AdminUserService
      ]
    });
    service = TestBed.inject(AdminUserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });
  it('should return user by id ', () => {
    const mockUser: User = { id: 1, email: 'test@mail.com', pseudo: 'testuser', roles: ['USER'], dateOfBirth: '', firstname: 'first', lastname: 'last' };
    const userId = '1';
    service.getUserById(userId).subscribe( response => {
      expect(response).toEqual(mockUser);
    });
    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });
  it('should return all users', () => {
    const mockUsers: User[] = [{ id: 1, email: 'test@mail.com', pseudo: 'testuser', roles: ['USER'], dateOfBirth: '', firstname: 'first', lastname: 'last' },
     { id: 2, email: 'john@mail.com', pseudo: 'johnD', roles: ['USER'], dateOfBirth: '', firstname: 'John', lastname: 'Doe' }];
    service.getAllUsers().subscribe( response => {
      expect(response).toEqual(mockUsers);
    });
    const req = httpMock.expectOne(`${service['pathService']}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUsers);
  });
  it(' should update user information', () => {
    const mockFormData = new FormData();
    mockFormData.append('pseudo','newPseudo');
    const mockMessageResponse: MessageResponse = { message: 'Updated with success!' };
    const userId = '1';
    service.updateUser(mockFormData, userId).subscribe( response => {
      expect(response).toEqual(mockMessageResponse);
    });
    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('PUT');
    req.flush(mockMessageResponse);

  });
  it('should delete a user', () => {
    const userId = 1;
    
    service.deleteUser(userId).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
