package fr.sirine.cuisine.recipe_ingredient;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.recipe.Recipe;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    public void createAndSaveRecipeIngredients(List<Ingredient> ingredients,
                                               Recipe recipe,
                                               List<IngredientRequest> ingredientRequests) {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            IngredientRequest ingredientRequest = ingredientRequests.get(i);

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(ingredientRequest.getQuantity());
            recipeIngredient.setUnit(ingredientRequest.getUnit());

            recipeIngredients.add(recipeIngredient);
        }

        // Enregistrer tous les RecipeIngredient
        recipeIngredientRepository.saveAll(recipeIngredients);
    }


    public void deleteRecipeIngredient(Integer id) {
        recipeIngredientRepository.deleteById(id);
    }

    public List<IngredientDto> getIngredientsForRecipe(Integer recipeId) {
        // récupérer les ingrédients d'une recette donnée
        return recipeIngredientRepository.findAll()
                .stream()
                .filter(ri -> ri.getRecipe().getId().equals(recipeId))
                .map(recipeIngredientMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean isShared(RecipeIngredient recipeIngredient) {
        Ingredient ingredient = recipeIngredient.getIngredient();
            // Vérifier si l'ingrédient est utilisé ailleurs
        return recipeIngredientRepository.existsByIngredientId(ingredient.getId());
    }

    public void addRecipeIngredient(Recipe recipe, Ingredient ingredient, Double quantity, String unit) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(quantity);
        recipeIngredient.setUnit(unit);
        recipeIngredientRepository.save(recipeIngredient);
    }
}