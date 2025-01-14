import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { AdminUserService } from '../../service/admin-user.service';
import { of } from 'rxjs';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockUserService: Partial<AdminUserService>;
  let mockActivatedRoute: Partial<ActivatedRoute>;

  beforeEach(async () => {
    // Mock de ActivatedRoute avec un paramètre d'ID
    const mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('123'),
          has: jest.fn().mockReturnValue(true),  // Ajout de la méthode has
          getAll: jest.fn().mockReturnValue([]), // Ajout de la méthode getAll
          keys: []                               // Ajout de la propriété keys
        } as ParamMap  // Cast explicite pour correspondre à l'interface ParamMap
      }
    };

    // Mock du service AdminUserService
    mockUserService = {
      getUserById: jest.fn().mockReturnValue(of({
        id: 123,
        name: 'John Doe',
        email: 'john.doe@example.com'
      }))
    };

    await TestBed.configureTestingModule({
      imports: [DetailComponent],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: AdminUserService, useValue: mockUserService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user details on init', () => {
    // Appel de ngOnInit()
    component.ngOnInit();
    
    // Vérification que le service a été appelé avec l'ID correct
    expect(mockUserService.getUserById).toHaveBeenCalledWith('123');
    
    // Vérification que les données de l'utilisateur sont bien récupérées
    expect(component.user).toEqual({
      id: 123,
      name: 'John Doe',
      email: 'john.doe@example.com'
    });
  });
});
