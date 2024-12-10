import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeDetailComponent } from './recipe-detail.component';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';

describe('RecipeDetailComponent', () => {
  let component: RecipeDetailComponent;
  let fixture: ComponentFixture<RecipeDetailComponent>;
  let mockActivatedRoute: Partial<ActivatedRoute>;


  beforeEach(async () => {
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
    await TestBed.configureTestingModule({
      imports: [RecipeDetailComponent],
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClient(), // Nouvelle API pour les tests HTTP
        { provide: ActivatedRoute, useValue: mockActivatedRoute },      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
