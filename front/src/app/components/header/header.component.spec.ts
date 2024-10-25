import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import { SessionService } from '../../service/session.service';
import { Router } from '@angular/router';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  let mockSessionService: Partial<SessionService>;

  beforeEach(async () => {
    mockSessionService = {
      user: { id: 1, email: 'admin@mail.fr', pseudo: 'admin', roles: ['ADMIN'], firstname: 'first', lastname: 'last', dateOfBirth: '' },    
      userRole: ['ADMIN'] // Mock role data
    };

    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    component.userRole = mockSessionService.userRole!;
    fixture.detectChanges();
  });
  it('should set user and isAdmin correctly on init', () => {
    component.ngOnInit();
    expect(component.user).toEqual(mockSessionService.user);
    expect(component.isAdmin).toBe(true);
  });

  it('should navigate to /me on navigateToMe', () => {
    const router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate');
    component.navigateToMe();
    expect(router.navigate).toHaveBeenCalledWith(['/me']);
  });
  it('should navigate to /admin/users-list on navigateToUserList', () => {
    const router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate');
    component.navigateToUserList();
    expect(router.navigate).toHaveBeenCalledWith(['admin/users-list']);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
