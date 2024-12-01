import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RecipeRequest } from '../interface/api/recipeRequest';
import { IngredientRequest } from '../interface/api/ingredientRequest';
import { Observable } from 'rxjs';
import { MessageResponse } from '../../../interface/api/messageResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private pathService = 'http://localhost:8080/api';

  constructor(private httpClient: HttpClient) { }

  public addRecipe(recipeRequest: RecipeRequest, ingredientRequests: IngredientRequest[], imageFile?: File): Observable<MessageResponse>{
    const formData: FormData = new FormData();
    formData.append('recipeRequest', new Blob([JSON.stringify(recipeRequest)], { type: 'application/json' }));
    formData.append('ingredientRequests', new Blob([JSON.stringify(ingredientRequests)], { type: 'application/json' }));

    if (imageFile) { 
      formData.append('imageFile', imageFile); 
    }
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.httpClient.post<MessageResponse>(`${this.pathService}/recipes/add`, formData, { headers });
  }
  public autoComplete(query: string): Observable<string[]>{
    console.log(`Query: ${query}`); // Debug
    return this.httpClient.get<string[]>(`${this.pathService}/ingredients/autocomplete?query=${query}`);
  }
}
