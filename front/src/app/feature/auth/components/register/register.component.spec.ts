import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from '../../services/auth.service';
import { FormBuilder } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { RegisterRequest } from '../../interfaces/register-request';
import { Component } from '@angular/core';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let registerRequest: RegisterRequest = {
    firstname: "first",
    lastname: "last",
    pseudo: "pseudo",
    dateOfBirth: "23/03/2011",
    email: "test@mail.fr",
    password: 'password'
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RegisterComponent, // Importez le composant standalone ici
      ],
      providers: [
        AuthService,
        FormBuilder,
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
      ]
    }).compileComponents();
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    expect(component).toBeTruthy();
    authService = TestBed.inject(AuthService);
  });

  function updateForum(firstname: string, lastname: string, pseudo: string, dateOfBirth: string, email: string, password: string){
    component.form.controls['firstname'].setValue(firstname);
    component.form.controls['lastname'].setValue(lastname);
    component.form.controls['pseudo'].setValue(pseudo);
    component.form.controls['dateOfBirth'].setValue(dateOfBirth);
    component.form.controls['email'].setValue(email);
    component.form.controls['password'].setValue(password);
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should register with success', () => {
    let authServiceSpy = jest.spyOn(authService, 'register');

    updateForum("first", "last", "pseudo", "23/03/2011", "test@mail.fr", "password");

    component.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(registerRequest);
    
  });
  it('should unsubscribe from httpSubscription on destroy', () => {
    // Mock an observable subscription
    const unsubscribeSpy = jest.fn();
    component.httpSubscription = { unsubscribe: unsubscribeSpy } as any;
  
    component.ngOnDestroy();
  
    expect(unsubscribeSpy).toHaveBeenCalled();
  });
});
