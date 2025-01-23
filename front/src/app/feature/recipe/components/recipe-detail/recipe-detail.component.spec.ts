import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeDetailComponent } from './recipe-detail.component';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RecipeService } from '../../service/recipe.service';
import { of } from 'rxjs';
import { CommentService } from '../../service/comment.service';
import { SessionService } from '../../../../service/session.service';

describe('RecipeDetailComponent', () => {
  let component: RecipeDetailComponent;
  let fixture: ComponentFixture<RecipeDetailComponent>;
  let mockActivatedRoute: Partial<ActivatedRoute>;
  let mockRecipeService: Partial<RecipeService>;
  let mockCommentService: Partial<CommentService>;
  let mockSessionService: Partial<SessionService>;


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
    mockRecipeService = {
      getRecipeById: jest.fn().mockReturnValue(of({
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
      }))
    };
    await TestBed.configureTestingModule({
      imports: [RecipeDetailComponent],
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClient(), // Nouvelle API pour les tests HTTP
        { provide: ActivatedRoute, useValue: mockActivatedRoute },     
        { provide: RecipeService, useValue: mockRecipeService }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should fetch recipe detail on init', () => {
    component.ngOnInit();
    expect(mockRecipeService.getRecipeById).toHaveBeenCalledWith(123);
    expect(component.recipe).toEqual({
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
    })
  });
});
