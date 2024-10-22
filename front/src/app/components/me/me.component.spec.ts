import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideAnimations } from '@angular/platform-browser/animations';

import { MeComponent } from './me.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from '../../service/session.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { UserService } from '../../service/user.service';
import { provideRouter } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: Partial<SessionService>;

  beforeEach(async () => {
    mockSessionService = {
      user: {
        id: 1,
        email: 'test@mail.com',
        pseudo: 'testuser',
        roles: ['USER'],
        firstname: 'Test',
        lastname: 'User',
        dateOfBirth: '2000-01-01'
      }
    }
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatSnackBarModule
      ],
      providers: [
        provideRouter([]),
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(),
        SessionService,
        UserService,
        provideAnimations()
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


