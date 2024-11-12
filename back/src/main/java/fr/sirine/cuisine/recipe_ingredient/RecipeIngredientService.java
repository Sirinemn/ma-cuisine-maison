package fr.sirine.cuisine.recipe_ingredient;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public RecipeIngredient addRecipeIngredient(RecipeIngredient recipeIngredient) {
        return recipeIngredientRepository.save(recipeIngredient);
    }

    public void deleteRecipeIngredient(Integer id) {
        recipeIngredientRepository.deleteById(id);
    }

    public List<RecipeIngredient> getIngredientsForRecipe(Integer recipeId) {
        // récupérer les ingrédients d'une recette donnée
        return recipeIngredientRepository.findAll()
                .stream()
                .filter(ri -> ri.getRecipe().getId().equals(recipeId))
                .collect(Collectors.toList());
    }

}

