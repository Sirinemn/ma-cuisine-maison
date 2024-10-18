import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginRequest } from '../../interfaces/login-request';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interface/session-information';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/service/session.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let loginRequest: LoginRequest= {
    email: 'test@mail.fr',
    password: 'password-test',
  };
  let mockSessionInformation: SessionInformation = {
    token: 'fake-token',
    userId: 1,
    pseudo: 'pseudo',

  };
  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [LoginComponent,
      BrowserAnimationsModule
    ],
    providers: [
      provideHttpClient(), // Nouvelle API pour les clients HTTP
      provideHttpClientTesting(),
      SessionService,
      AuthService
    ]
}).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  function updateForum(email: string, password: string){
    component.form.controls['email'].setValue(email);
    component.form.controls['password'].setValue(password);
  }
  it("should login with success", () =>{
    updateForum("test@mail.fr","password-test");
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let authServiceSpy1 = jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation));
    let authServiceSpy2 = jest.spyOn(authService, 'me');


    component.submit();

    expect(authServiceSpy1).toHaveBeenCalledWith(loginRequest);
    expect(authServiceSpy2).toHaveBeenCalled();
  })
  it('should set errorMessage on login failure', () => {
    const errorResponse = { error: { error: 'Invalid credentials' } };
  
    // Mock the AuthService login method to return an error
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => errorResponse));
  
    component.form.setValue({ email: 'test@example.com', password: 'password' });
    component.submit();
  
    expect(component.errorMessage).toBe('Invalid credentials');
  });
  it('should unsubscribe from httpSubscription on destroy', () => {
    // Mock an observable subscription
    const unsubscribeSpy = jest.fn();
    component.httpSubscription = { unsubscribe: unsubscribeSpy } as any;
  
    component.ngOnDestroy();
  
    expect(unsubscribeSpy).toHaveBeenCalled();
  });
  
});
