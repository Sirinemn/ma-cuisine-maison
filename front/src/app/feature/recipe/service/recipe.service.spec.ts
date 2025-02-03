import { TestBed } from '@angular/core/testing';
import { RecipeService } from './recipe.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Recipe } from '../interface/recipe';
import { Ingredient } from '../interface/ingredient';
import { RecipeRequest } from '../interface/api/recipeRequest';
import { IngredientRequest } from '../interface/api/ingredientRequest';
import { MessageResponse } from '../../../interface/api/messageResponse.interface';

describe('RecipeService', () => {
  let service: RecipeService;
  let httpMock: HttpTestingController;
  let ingredientMock: Ingredient = {
    name: 'tomate',
    quantity: 500,
    unit: 'grammee'
  }
  let recipeMock : Recipe = {
    categoryName : 'ENTREES',
    cookingTime: 5,
    description: 'description',
    imageId: 1,
    imageName: 'image',
    imageThumbName: 'thumbImage',
    ingredients: [ingredientMock],
    servings: 5,
    title: 'title',
    userId: 2,
    userPseudo: 'pseudo',
    id: 1
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
      ]
    });
    service = TestBed.inject(RecipeService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  afterEach(() => {
    httpMock.verify();
  });
  it('should return all recipes', () => {
    let recipeMockList = [recipeMock];
    service.getAllRecipes().subscribe( response => {
      expect(response).toEqual(recipeMockList);
    });
    const req = httpMock.expectOne(`${service['pathService']}/recipes/recipes-list`);
    expect(req.request.method).toBe('GET');
    req.flush(recipeMockList);
  });
  it('should return recipe by id', () => {
    service.getRecipeById(1).subscribe( response => {
      expect(response).toEqual(recipeMock);
    });
    const req = httpMock.expectOne(`${service['pathService']}/recipes/recipe/1`);
    expect(req.request.method).toBe('GET');
    req.flush(recipeMock);
  });
  it('should return recipe by category name', () => {
    let recipeMockList = [recipeMock];
    service.getRecipeByCategory('ENTREES').subscribe( response => {
      expect(response).toEqual(recipeMock);
    });
    const req = httpMock.expectOne(`${service['pathService']}/recipes/category?categoryName=ENTREES`);
    expect(req.request.method).toBe('GET');
    req.flush(recipeMockList);
  });
  it('should return recipe by user id', () => {
    let recipeMockList = [recipeMock];
    service.getRecipeByUserId(2).subscribe( response => {
      expect(response).toEqual(recipeMockList);
    });
    const req = httpMock.expectOne(`${service['pathService']}/recipes/user?userId=2`);
    expect(req.request.method).toBe('GET');
    req.flush(recipeMockList);
  });
  it('should delete recipe', () => {
    service.deleteRecipe(1).subscribe( response => {
      expect(response).toBeNull();
    })
    const req = httpMock.expectOne(`${service['pathService']}/recipes/recipe/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
  it('should autocomplete ingredients', () => {
    const query = 'sugar';
    const autocompleteResults: string[] = ['sugar', 'sugarcane', 'sugar syrup'];

    service.autoComplete(query).subscribe(results => {
      expect(results.length).toBe(3);
      expect(results).toEqual(autocompleteResults);
    });

    const req = httpMock.expectOne(`${service['pathService']}/ingredients/autocomplete?query=${query}`);
    expect(req.request.method).toBe('GET');
    req.flush(autocompleteResults);
  });
  it('should add a new recipe', () => {
    const recipeRequest: RecipeRequest = { 
      categoryName: 'ENTREES',
      cookingTime: 5,
      description: 'description',
      serving: 5,
      title: 'title',
      userId: 1,
      userPseudo: 'pseudo' 
    };
    const ingredientRequests: IngredientRequest[] = [];
    const messageResponse: MessageResponse = { 
      message: 'Recipe added successfully' 
    };

    service.addRecipe(recipeRequest, ingredientRequests).subscribe(res => {
      expect(res.message).toBe('Recipe added successfully');
    });

    const req = httpMock.expectOne(`${service['pathService']}/recipes/add`);
    expect(req.request.method).toBe('POST');
    req.flush(messageResponse);
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
