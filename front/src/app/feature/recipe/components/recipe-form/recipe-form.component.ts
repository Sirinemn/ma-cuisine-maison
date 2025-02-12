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
  disableButton : Boolean = false;

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
    const file = event.target.files[0];
    if (file) {
      // Validate file type
      if (!file.type.match(/^image\/(jpeg|png|gif)$/)) {
        this.snackBar.open('Veuillez sélectionner une image valide (JPEG, PNG, ou GIF)', 'OK', { 
          duration: 3000 
        });
        return;
      }
      
      // Validate file size (e.g., 5MB max)
      const maxSize = 5 * 1024 * 1024; // 5MB in bytes
      if (file.size > maxSize) {
        this.snackBar.open('L\'image ne doit pas dépasser 5MB', 'OK', { 
          duration: 3000 
        });
        return;
      }
      
      this.imageFile = file;
    }
  }

  addIngredient(): void {
    if (this.ingredientsForm.valid) {
      const ingredient = this.ingredientsForm.value;
      this.ingredientList.push(ingredient);
      this.ingredientsForm.reset(); 
    }
  }

  private resetForm(): void {
    this.recipeForm.reset();
    this.ingredientsForm.reset();
    this.ingredientList = [];
    this.imageFile = null;
  }

  submitForm(): void {
    if (this.recipeForm.valid && this.ingredientList.length > 0) {
      try {
        const recipeRequest = this.recipeForm.value as RecipeRequest;
        this.user = this.sessionService.user!;
        recipeRequest.userId = this.user.id!;
        recipeRequest.userPseudo = this.user.pseudo;
        
        const subscription = this.recipeService
          .addRecipe(recipeRequest, this.ingredientList, this.imageFile || undefined)
          .subscribe({
            next: (response) => {
              this.snackBar.open(response.message, 'OK', { duration: 3000 });
              this.resetForm();
              this.disableButton = true;
              setTimeout(() => this.router.navigate(['recipe/list']), 3000);
            },
            error: (error) => {
              let message = 'Une erreur est survenue. Veuillez réessayer plus tard.';
              
              if (error.status === 413) {
                message = 'Le fichier téléchargé dépasse la taille maximale autorisée';
              } else if (error.status === 415) {
                message = 'Le fichier fourni n\'est pas une image valide';
              }
              
              this.snackBar.open(message, 'OK', { duration: 3000 });
            }
          });
          
        this.httpSubscriptions.push(subscription);
      } catch (error) {
        this.snackBar.open('Erreur lors de la préparation des données', 'OK', { 
          duration: 3000 
        });
      }
    } else {
      this.snackBar.open(
        'Formulaire de recette invalide ou aucun ingrédient ajouté', 
        'OK', 
        { duration: 3000 }
      );
    }
  }

  public back() {
    window.history.back();
  }
  ngOnDestroy(): void {
    this.httpSubscriptions.forEach(subscribtion=> subscribtion.unsubscribe());
  } 
}
