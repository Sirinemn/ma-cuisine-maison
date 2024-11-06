package fr.sirine.cuisine.mapper;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientMapper;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientMapperTest {

    @InjectMocks
    private IngredientMapper ingredientMapper;

    @Mock
    private RecipeService recipeService;

    private Recipe recipe;
    private Ingredient ingredient;
    private IngredientDto ingredientDto;

    @BeforeEach
    public void setUp(){
        recipe = Recipe.builder().id(1).title("Test Recipe").build();

        ingredient = Ingredient.builder()
                .id(1)
                .name("Name")
                .recipe(recipe)
                .build();

        ingredientDto = IngredientDto.builder()
                .name("Name")
                .build();
    }

    @Test
    void testToEntity(){
        when(recipeService.getRecipeById(1)).thenReturn(recipe);

        Ingredient result = ingredientMapper.toEntity(ingredientDto);

        assertEquals(ingredientDto.getId(), result.getId());
        assertEquals(ingredientDto.getName(), result.getName());

    }

    @Test
    void testToDto(){
        IngredientDto result = ingredientMapper.toDto(ingredient);

        assertEquals(ingredient.getId(), result.getId());
        assertEquals(ingredient.getName(), result.getName());

    }
}
