package fr.sirine.cuisine.recipe;


import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientRepository;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
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

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }


    public Recipe createRecipe(RecipeDto recipeDto) {
        // Convert RecipeDto to Recipe entity
        Recipe recipe = recipeMapper.toEntity(recipeDto);
        recipe = recipeRepository.save(recipe);

        // Create and save RecipeIngredient entities
        for (IngredientDto ingredientDto : recipeDto.getIngredients()) {
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
