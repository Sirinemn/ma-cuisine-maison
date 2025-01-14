import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeListComponent } from './recipe-list.component';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeService } from '../../service/recipe.service';
import { of } from 'rxjs';

describe('RecipeListComponent', () => {
  let component: RecipeListComponent;
  let fixture: ComponentFixture<RecipeListComponent>;
  let mockRouter: Partial<Router>;
  let mockRecipeService: Partial<RecipeService>;
  let mockActivatedRoute: Partial<ActivatedRoute>;
  
  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn()
    };
    const mockActivatedRoute = {
      queryParams: of({ category: 'ENTREES' }) 
    };
    mockRecipeService = {
          getAllRecipes: jest.fn().mockReturnValue(of([{
            id: 1,
            title: 'title',
            description: 'description',
            cookingTime: 5,
            servings: 5,
            userId: 1,
            userPseudo: 'pseudo',
            categoryName: 'ENTREES',
            ingredients: [],
            imageId: 1,
            imageName: 'imageName',
            imageThumbName: 'imageThumb'
          },
        {
          id: 2,
            title: 'title',
            description: 'description',
            cookingTime: 5,
            servings: 5,
            userId: 1,
            userPseudo: 'pseudo',
            categoryName: 'DESSERT',
            ingredients: [],
            imageId: 1,
            imageName: 'imageName',
            imageThumbName: 'imageThumb'
        }])),
          getRecipeByCategory: jest.fn().mockReturnValue(of([{
            id: 123,
            title: 'title',
            description: 'description',
            cookingTime: 5,
            servings: 5,
            userId: 1,
            userPseudo: 'pseudo',
            categoryName: 'ENTREES',
            ingredients: [],
            imageId: 1,
            imageName: 'imageName',
            imageThumbName: 'imageThumb'
          }]))
        };
    await TestBed.configureTestingModule({
      imports: [RecipeListComponent],
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClient(), // Nouvelle API pour les tests HTTP
        { provide: ActivatedRoute, useValue: mockActivatedRoute },     
        { provide: RecipeService, useValue: mockRecipeService },
        { provide: Router, useValue: mockRouter }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should load recipes by category on init', () => {
    component.ngOnInit();
    expect(mockRecipeService.getRecipeByCategory).toHaveBeenCalled();
  });
  it('should navigate to view reipe', () => {
    const recipeId = 1;
    component.viewDetails(recipeId);
    expect(mockRouter.navigate).toHaveBeenCalledWith([`recipe/detail/${recipeId}`]);
  });
  it('should load all recipes on init if no category is provided', () => {
    // Simuler l'absence de catégorie dans les query params
    mockActivatedRoute = {
      queryParams: of({})
    };
    // Recréez le module de test avec les query params mis à jour 
    TestBed.resetTestingModule(); 
    TestBed.configureTestingModule({ 
      imports: [RecipeListComponent], 
      providers: [ 
        { provide: ActivatedRoute, useValue: mockActivatedRoute }, 
        { provide: RecipeService, useValue: mockRecipeService }, 
        { provide: Router, useValue: mockRouter } 
      ] }).compileComponents(); 
      fixture = TestBed.createComponent(RecipeListComponent);
       component = fixture.componentInstance;
    
    // Initialisez le composant et détectez les changements
    component.ngOnInit();
    fixture.detectChanges();
    
    // Vérifiez si la méthode getAllRecipes a été appelée
    expect(mockRecipeService.getAllRecipes).toHaveBeenCalled();
  });
  

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
