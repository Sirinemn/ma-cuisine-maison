import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-recipe-form',
  standalone: true,
  imports: [
    MatCardModule,
  ],
  templateUrl: './recipe-form.component.html',
  styleUrl: './recipe-form.component.scss'
})
export class RecipeFormComponent {

  public back() {
    window.history.back();
  }
  public Submit(){
    
  }

}
