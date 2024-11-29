import { TestBed, ComponentFixture } from '@angular/core/testing';
import { RecipeFormComponent } from './recipe-form.component';
import { provideAnimations } from '@angular/platform-browser/animations';
import { RecipeService } from '../../service/recipe.service';
import { of } from 'rxjs';

describe('RecipeFormComponent', () => {
  let component: RecipeFormComponent;
  let fixture: ComponentFixture<RecipeFormComponent>;

  let mockRecipeService: Partial<RecipeService>;

  beforeEach(async () => {
    mockRecipeService = {
      addRecipe: jest.fn().mockReturnValue(of({ message: 'Recipe added successfully!' }))
    };

    await TestBed.configureTestingModule({
      imports: [
        RecipeFormComponent
      ],
      providers: [
        { provide: RecipeService, useValue: mockRecipeService },
        provideAnimations()

      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RecipeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
