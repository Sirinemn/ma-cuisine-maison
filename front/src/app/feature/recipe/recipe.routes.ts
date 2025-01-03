import { Routes } from "@angular/router";
import { RecipeFormComponent } from "./components/recipe-form/recipe-form.component";
import { RecipeListComponent } from "./components/recipe-list/recipe-list.component";
import { RecipeDetailComponent } from "./components/recipe-detail/recipe-detail.component";

export const recipe_routes: Routes = [
    {
        title: 'Add',
        path: 'add-recipe',
        component: RecipeFormComponent
    },
    {
        title: 'List',
        path: 'list',
        component: RecipeListComponent
    },
    {
        title: 'Detail',
        path: 'detail/:id',
        component: RecipeDetailComponent
    }
]