import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WelcomeComponent } from './welcome.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { SessionService } from '../../../service/session.service';
import { Router } from '@angular/router';

describe('WelcomeComponent', () => {
  let component: WelcomeComponent;
  let fixture: ComponentFixture<WelcomeComponent>;
  let mockSessionService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockSessionService = {
      user: { pseudo: 'TestUser' },
      logOut: jest.fn(),
    };
    mockRouter = {
      navigate: jest.fn()  // Mock de la fonction navigate
    };
    await TestBed.configureTestingModule({
      imports: [WelcomeComponent],
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WelcomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the pseudo', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Bienvenue TestUser');
  });

  it('should call logOut on sessionService when logOut is clicked', () => {
    jest.spyOn(component, 'logOut');
    component.logOut();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
