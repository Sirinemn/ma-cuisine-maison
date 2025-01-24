import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RecipeDetailComponent } from './recipe-detail.component';
import { provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { RecipeService } from '../../service/recipe.service';
import { of } from 'rxjs';
import { CommentService } from '../../service/comment.service';
import { SessionService } from '../../../../service/session.service';
import { Recipe } from '../../interface/recipe';
import { MessageResponse } from '../../../../interface/api/messageResponse.interface';


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
    mockCommentService = {
      getRecipeComments: jest.fn().mockReturnValue(of({
        comment: [
          {
            id: 1,
            content: "dummy comment",
            userId: 1,
            userPseudo: "pseudo",
            recipeId: 123
          }
        ]
      }))
    };
    mockSessionService = {
      user: { 
        id: 1, 
        pseudo: 'testUser',
        email: 'email.com',
        roles: [],
        firstname: 'first',
        lastname: 'last',
        dateOfBirth: ''
      }
    };
    await TestBed.configureTestingModule({
      imports: [RecipeDetailComponent],
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClient(), // Nouvelle API pour les tests HTTP
        { provide: ActivatedRoute, useValue: mockActivatedRoute },     
        { provide: RecipeService, useValue: mockRecipeService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: CommentService, useValue : mockCommentService}
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
  it('should fetch comments', () => {
    component.recipe = {id: 123} as Recipe;
    component.loadComments();
    expect(mockCommentService.getRecipeComments).toHaveBeenCalled();
    expect(component.comments).toEqual([{
      id: 1,
      content: "dummy comment",
      userId: 1,
      userPseudo: "pseudo",
      recipeId: 123
    }])
  });
  it('should add a comment and display the success message', () => {
    // Mock sessionService to return a logged-in user
    const mockUser = { 
      id: 1, 
      pseudo: 'testUser',  
      email : 'email.com',
      roles: [],
      firstname : 'first', 
      lastname: 'last',
      dateOfBirth: ''
    };
    mockSessionService = { user: mockUser };
  
    // Set up a recipe and bind it to the component
    component.recipe = { id: 123 } as Recipe;
  
    // Populate the comment form with valid data
    component.commentForm.setValue({ content: 'Test comment' });
  
    // Mock the addComment method to return a success message
    const mockMessageResponse = { message: 'Comment added successfully' } as MessageResponse;
    mockCommentService.addComment = jest.fn().mockReturnValue(of(mockMessageResponse));
  
    // Spy on loadComments to verify it's called
    const loadCommentsSpy = jest.spyOn(component, 'loadComments');
  
    // Call the addComment method
    component.addComment();
  
    // Expectations
    expect(mockCommentService.addComment).toHaveBeenCalledWith({
      content: 'Test comment',
      recipeId: 123,
      userId: mockUser.id,
      userPseudo: mockUser.pseudo,
    });
  
    expect(loadCommentsSpy).toHaveBeenCalled(); // Verify comments are reloaded
    expect(component.commentForm.valid).toBe(false); // The form should be reset
  });
  
});
