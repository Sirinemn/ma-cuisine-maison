package fr.sirine.cuisine.recipe;


import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
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
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }


    public Recipe createRecipe(String title, String description, int cookingTime, int servings,
                               Integer userId, String categoryName, List<IngredientDto> ingredientDtos
                               ) {
        // Convert RecipeDto to Recipe entity
        // Créer l'objet Recipe
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setCookingTime(cookingTime);
        recipe.setServings(servings);

        // Create and save RecipeIngredient entities
        for (IngredientDto ingredientDto : recipeIngredientMapper.toDto(recipe.getIngredients())) {
            Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getName())
                    .orElseGet(() -> ingredientRepository.save(new Ingredient(ingredientDto.getName())));

            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .recipe(recipe)
                    .ingredient(ingredient)
                    .quantity(ingredientDto.getQuantity())
                    .unit(ingredientDto.getUnit())
                    .build();

            recipeIngredientRepository.save(recipeIngredient);
        }

        return recipe;
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
