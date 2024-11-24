package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    RecipeService recipeService;
    @Mock
    RecipeMapper recipeMapper;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    IngredientService ingredientService;

    @Test
    void getAllRecipesTest() {
        Recipe recipe = Recipe.builder()
                .id(1)
                .build();
        RecipeDto recipeDto = RecipeDto.builder()
                .id(1)
                .build();
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDto);

        List<RecipeDto> result = recipeService.getAllRecipes();
        System.out.println(result);
        assertEquals(result.get(0).getId(), recipe.getId());
    }
    @Test
    void deleteRecipeTest() {
        Integer id = 1;

        recipeService.deleteRecipe(id);

        verify(recipeRepository, times(1)).deleteById(id);
    }
    @Test
    void getRecipeByIdTest() {
        Integer id = 1;
        Recipe recipe = Recipe.builder()
                .id(1)
                .title("title")
                .build();
        when(recipeRepository.findById(id)).thenReturn(Optional.ofNullable(recipe));

        Recipe result = recipeService.getRecipeById(id);

        assertEquals("title", result.getTitle());
    }

    @Test public void testCreateRecipe() {
        Recipe recipe = Recipe.builder()
                .id(1)
                .title("title")
                .build();

        Ingredient ingredient = Ingredient.builder()
                .name("tomate")
                .build();

        IngredientDto ingredientDto = IngredientDto.builder()
                .unit("gr")
                .quantity(100.0)
                .name("tomate")
                .build();
        RecipeDto recipeDto = RecipeDto.builder()
                .id(1)
                .ingredients(List.of(ingredientDto))
                .build();

        when(recipeMapper.toEntity(any(RecipeDto.class))).thenReturn(recipe);
        when(ingredientService.findOrCreateIngredient(anyString())).thenReturn(ingredient);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        Recipe result = recipeService.createRecipe(recipeDto);

        assertNotNull(result);
        assertEquals(recipe.getId(), result.getId());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}
