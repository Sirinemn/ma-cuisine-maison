package fr.sirine.cuisine.recipe_ingredient;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
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

    @Mock
    RecipeIngredientMapper recipeIngredientMapper;

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
        IngredientDto ingredientDto = IngredientDto.builder()
                .name("Tomate")
                .build();
        when(recipeIngredientRepository.findAll()).thenReturn(List.of(recipeIngredient));
        when(recipeIngredientMapper.toDto(recipeIngredient)).thenReturn(ingredientDto);
        List<IngredientDto> result = recipeIngredientService.getIngredientsForRecipe(recipeId);

        assertNotNull(result);
        assertEquals("Tomate", result.get(0).getName());
        assertEquals(1, result.size());
    }

    @Test
    void isSharedTest() {
        Ingredient ingredient = Ingredient.builder()
                .id(1)
                .name("Sugar")
                .build();
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .recipeIngredientId(1)
                .ingredient(ingredient)
                .build();

        recipeIngredientService.isShared(recipeIngredient);

        verify(recipeIngredientRepository, times(1)).existsByIngredientId(1);
    }
}
