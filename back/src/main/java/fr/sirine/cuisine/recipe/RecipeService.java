package fr.sirine.cuisine.recipe;


import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientMapper;
import fr.sirine.cuisine.ingredient.IngredientRepository;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientMapper;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientMapper ingredientMapper;


    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, IngredientMapper ingredientMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientMapper = ingredientMapper;

    }

    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setServings(recipeDto.getServings());

        // Mapper et ajouter les ingrédients
        List<RecipeIngredient> ingredients = recipeDto.getIngredients().stream()
                .map(ingredientDto -> {
                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredient(ingredientMapper.toEntity(ingredientDto));
                    recipeIngredient.setQuantity(ingredientDto.getQuantity());
                    recipeIngredient.setUnit(ingredientDto.getUnit());
                    recipeIngredient.setRecipe(recipe); // Associer la recette
                    return recipeIngredient;
                })
                .collect(Collectors.toList());
        recipe.setIngredients(ingredients);

        // Enregistrer la recette avec les ingrédients associés
        return recipeRepository.save(recipe);
    }

    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteRecipe(Integer id) {
        recipeRepository.deleteById(id);
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId).orElse(null);
    }
}
