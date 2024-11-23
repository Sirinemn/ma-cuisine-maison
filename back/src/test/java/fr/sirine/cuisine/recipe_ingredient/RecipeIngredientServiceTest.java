package fr.sirine.cuisine.recipe_ingredient;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.recipe.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RecipeIngredientServiceTest {

    @InjectMocks
    RecipeIngredientService recipeIngredientService;

    @Mock
    RecipeIngredientRepository recipeIngredientRepository;

    @Test
    void createAndSaveRecipeIngredientsTest(){
        Ingredient ingredient = Ingredient.builder()
                .name("tomate")
                .build();
        Recipe recipe = Recipe.builder()
                .title("Pasta")
                .build();
        IngredientRequest ingredientRequest = IngredientRequest.builder()
                .unit("gr")
                .quantity(100.0)
                .name("tomate")
                .build();
        recipeIngredientService.createAndSaveRecipeIngredients(List.of(ingredient), recipe, List.of(ingredientRequest));
        verify(recipeIngredientRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testDeleteRecipeIngredient() {
        Integer id = 1;
        recipeIngredientService.deleteRecipeIngredient(id);
        verify(recipeIngredientRepository, times(1)).deleteById(id);
    }

    @Test
    void getIngredientsForRecipe(){
        Integer recipeId = 1;
        Recipe recipe = Recipe.builder()
                .id(1)
                .title("title")
                .servings(5)
                .build();
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .recipe(recipe)
                .build();
        when(recipeIngredientRepository.findAll()).thenReturn(List.of(recipeIngredient));

        List<RecipeIngredient> result = recipeIngredientService.getIngredientsForRecipe(recipeId);
        assertNotNull(result);
        assertEquals(result.get(0).getRecipe().getTitle(), recipe.getTitle());
    }
}
