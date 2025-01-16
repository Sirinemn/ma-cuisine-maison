import { Component, OnDestroy, OnInit } from '@angular/core';
import { RecipeService } from '../../service/recipe.service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { Recipe } from '../../interface/recipe';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIcon,
    RouterModule
  ],
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss'
})
export class RecipeDetailComponent implements OnInit, OnDestroy{
  private httpSubscriptions: Subscription[] = [];
  public recipe!: Recipe;

  constructor(
    private recipeService: RecipeService,
    private activateRoute: ActivatedRoute
  ){}

  ngOnInit(): void {
    const id =+ this.activateRoute.snapshot.paramMap.get('id')!;
    this.httpSubscriptions.push(
      this.recipeService.getRecipeById(id).subscribe(
        resultat => this.recipe = resultat
      )
    )
  }
  ngOnDestroy(): void {
    this.httpSubscriptions.forEach( sub => sub.unsubscribe());
  }

  public imgError(event: Event): void {
    const element = event.target as HTMLImageElement;
    element.src = 'assets/default-image.jpg'; // Chemin vers une image par défaut
  }  
  public back() {
    window.history.back();
  }
}
