import { TestBed } from '@angular/core/testing';

import { RecipeService } from './recipe.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('RecipeService', () => {
  let service: RecipeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Nouvelle API pour les clients HTTP
        provideHttpClientTesting(), // Nouvelle API pour les tests HTTP
      ]
    });
    service = TestBed.inject(RecipeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
