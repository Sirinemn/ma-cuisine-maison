import { TestBed } from '@angular/core/testing';

import { AdminUserServiceService } from './admin-user-service.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('AdminUserServiceService', () => {
  let service: AdminUserServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
      ]
    });
    service = TestBed.inject(AdminUserServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
