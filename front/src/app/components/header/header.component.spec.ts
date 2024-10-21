import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import { SessionService } from '../../service/session.service';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  let mockSessionService;

  beforeEach(async () => {
    mockSessionService = {
      user: { id: 1, email: 'admin@mail.fr', pseudo: 'admin', roles: ['ADMIN'] },    
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
    component.userRole = mockSessionService.userRole;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
