import { TestBed } from '@angular/core/testing';

import { SessionService } from './session.service';
import { User } from '../interface/user';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('SessionService', () => {
  let service: SessionService;
  let mockUser: User ={
    id: 1,
    pseudo: "pseudo",
    email: "test@mail.fr",
    roles: [],
    firstname: "first",
    lastname: "last",
    dateOfBirth: ""
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
      ]
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
  });

  it('should log in a user and update isLogged to true', () => {
    service.logIn(mockUser);

    expect(service.user).toBe(mockUser); // Vérifie que l'utilisateur est stocké
    expect(service.isLogged).toBe(true); // Vérifie que l'utilisateur est connecté
  });

  it('should log out a user and reset state', () => {
    service.logIn(mockUser); // Connecte d'abord l'utilisateur
    service.logOut();        // Déconnecte l'utilisateur

    expect(service.user).toBeUndefined(); // Vérifie que l'utilisateur est supprimé
    expect(service.isLogged).toBe(false); // Vérifie que l'utilisateur est déconnecté
  });
});
