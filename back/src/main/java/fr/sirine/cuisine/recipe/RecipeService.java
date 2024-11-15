package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientMapper;
import fr.sirine.cuisine.ingredient.IngredientService;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
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
    private final IngredientService ingredientService;


    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, IngredientMapper ingredientMapper, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientMapper = ingredientMapper;
        this.ingredientService = ingredientService;
    }
    @Transactional
    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setServings(recipeDto.getServings());

        // Mapper et ajouter les ingrédients en utilisant IngredientService
        List<RecipeIngredient> ingredients = recipeDto.getIngredients().stream()
                .map(ingredientDto -> {
                    Ingredient ingredient = ingredientService.findOrCreateIngredient(ingredientDto.getName());

                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredient(ingredient);
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
