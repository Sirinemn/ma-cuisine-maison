import { Routes } from "@angular/router";
import { RecipeFormComponent } from "./components/recipe-form/recipe-form.component";

export const recipe_routes: Routes = [
    {
        title: 'Add',
        path: 'add-recipe',
        component: RecipeFormComponent
    }
]