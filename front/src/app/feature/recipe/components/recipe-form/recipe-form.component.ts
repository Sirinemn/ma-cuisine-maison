import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RecipeService } from '../../service/recipe.service';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { SessionService } from '../../../../service/session.service';
import { User } from '../../../../interface/user';
import { RecipeRequest } from '../../interface/api/recipeRequest';
import { debounceTime, distinctUntilChanged, Observable, of, Subscription, switchMap } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

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
    MatIconModule,
    MatAutocompleteModule
  ],
  templateUrl: './recipe-form.component.html',
  styleUrls: ['./recipe-form.component.scss']
})
export class RecipeFormComponent implements OnDestroy, OnInit {
  private httpSubscriptions: Subscription[] = [];
  recipeForm: FormGroup;
  ingredientsForm: FormGroup;
  filteredIngredients$: Observable<string[]> = of([]);
  imageFile: File | null = null;
  user!: User;
  categories: string[] = ['ENTREES', 'PLATS_PRINCIPAUX', 'ACCOMPAGNEMENTS', 'DESSERTS', 'BOISSONS', 'PETITS_DEJEUNERS_BRUNCHS', 'CUISINE_DU_MONDE'];
  unities: string[] = ['Tasse', 'Gramme', 'Pièce', 'Cl', 'Ml', 'Litre', 'Cuillère à soupe', 'Cuillère à café', 'Sachet', 'Pincée'];
  ingredientList: { name: string, quantity: number, unit: string }[] = [];

  constructor(private fb: FormBuilder,
     private recipeService: RecipeService,
     private sessionService: SessionService,
     private snackBar: MatSnackBar,
     private router: Router
    ) {
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
  ngOnInit(): void { 
    this.filteredIngredients$ = this.ingredientsForm.get('ingredientName')!.valueChanges.pipe( 
      debounceTime(300), 
      distinctUntilChanged(), 
      switchMap(value => this.recipeService.autoComplete(value)) 
    ); 
  }

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
      this.httpSubscriptions.push(this.recipeService.addRecipe(recipeRequest, ingredientRequests, this.imageFile!).subscribe(
        response => {
          this.snackBar.open(response.message, 'OK', { duration: 3000 });
          // Réinitialise les formulaires et la liste d'ingrédients après soumission réussie
          this.recipeForm.reset();
          this.ingredientsForm.reset();
          this.ingredientList = [];
          this.imageFile = null;
          setTimeout(() => {
            this.router.navigate(['recipe/list']);
          }, 3000)
        },
        error => {
          if (error.status === 413) {
            this.snackBar.open('Le fichier téléchargé dépasse la taille maximale autorisée', 'OK', { duration: 3000} )
          } else if(error.status === 415) {
            this.snackBar.open('Le fichier fourni n\'est pas une image valide', 'ok', {duration: 3000 });
          } else {
            this.snackBar.open('Une erreur est survenue. Veuillez réessayer plus tard.', 'OK', { duration: 3000 });
          }                
        }
      ));
    } else {
      this.snackBar.open('Formulaire de recette invalide ou aucun ingrédient ajouté', 'OK', { duration: 3000 });
    }
  }

  public back() {
    window.history.back();
  }
  ngOnDestroy(): void {
    this.httpSubscriptions.forEach(subscribtion=> subscribtion.unsubscribe());
  } 
}
