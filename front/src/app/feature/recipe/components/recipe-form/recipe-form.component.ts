import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RecipeService } from '../../service/recipe.service';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { SessionService } from '../../../../service/session.service';
import { User } from '../../../../interface/user';
import { RecipeRequest } from '../../interface/api/recipeRequest';

@Component({
  selector: 'app-recipe-form',
  standalone: true,
  imports: [
    CommonModule, // Pour NgIf, NgFor, etc.
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule
  ],
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.scss']
})
export class RecipeFormComponent implements OnInit {
  recipeForm: FormGroup;
  ingredientsForm: FormGroup;
  imageFile: File | null = null;
  user!: User;
  categories: string[] = ['ENTREES', 'PLATS_PRINCIPAUX', 'ACCOMPAGNEMENTS', 'DESSERTS', 'BOISSONS', 'PETITS_DEJEUNERS_BRUNCHS', 'CUISINE_DU_MONDE'];
  ingredientList: { name: string, quantity: number, unit: string }[] = [];

  constructor(private fb: FormBuilder, private recipeService: RecipeService, private sessionService: SessionService) {
    this.recipeForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      cookingTime: ['', Validators.required],
      servings: ['', Validators.required],
      categoryName: ['', Validators.required]
    });

    this.ingredientsForm = this.fb.group({
      ingredientName: ['', Validators.required],
      quantity: ['', Validators.required],
      unit: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onFileChange(event: any): void {
    if (event.target.files.length > 0) {
      this.imageFile = event.target.files[0];
    }
  }

  addIngredient(): void {
    if (this.ingredientsForm.valid) {
      const ingredient = this.ingredientsForm.value;
      this.ingredientList.push(ingredient);
      this.ingredientsForm.reset(); 
    }
  }

  submitForm(): void {
    if (this.recipeForm.valid && this.ingredientList.length > 0) {
      const recipeRequest = this.recipeForm.value as RecipeRequest;
      this.user = this.sessionService.user!;
      recipeRequest.userId = this.user.id!;
      recipeRequest.userPseudo = this.user.pseudo;
      const ingredientRequests = this.ingredientList;
      console.log(recipeRequest, ingredientRequests);
      this.recipeService.addRecipe(recipeRequest, ingredientRequests, this.imageFile!).subscribe(
        response => {
          console.log(response.message);
          // Réinitialise les formulaires et la liste d'ingrédients après soumission réussie
          this.recipeForm.reset();
          this.ingredientsForm.reset();
          this.ingredientList = [];
          this.imageFile = null;
        },
        error => {
          console.error('There was an error!', error);
        }
      );
    } else {
      console.warn('Formulaire de recette invalide ou aucun ingrédient ajouté');
    }
  }

  public back() {
    window.history.back();
  }
}
