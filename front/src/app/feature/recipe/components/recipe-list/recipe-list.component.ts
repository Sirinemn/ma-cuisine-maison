import { Component, NgModule, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { RecipeService } from '../../service/recipe.service';
import { Recipe } from '../../interface/recipe';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-recipe-list',
  standalone: true,
  imports: [
    MatCardModule,
    CommonModule,
    MatIcon
  ],
  templateUrl: './recipe-list.component.html',
  styleUrl: './recipe-list.component.scss'
})
export class RecipeListComponent implements OnInit{
  public recipeList$!: Observable<Recipe[]>;

  constructor(
    private recipeService: RecipeService,
    private router: Router
  ){}

  ngOnInit(): void {
      this.recipeList$ = this.recipeService.getAllRecipes();  
  }
  public viewDetails(recipeId?: number): void{
    this.router.navigate([`recipe/detail/${recipeId}`]);
  }
  public back() {
    window.history.back();
  }

}
