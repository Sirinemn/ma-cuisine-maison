package fr.sirine.cuisine.mapper;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RecipeIngredientMapperTest {

    private final RecipeIngredientMapper recipeIngredientMapper = Mappers.getMapper(RecipeIngredientMapper.class);


    @Test
    void testToIngredientDto(){

        Ingredient ingredient = Ingredient.builder()
                .name("Sugar")
                .build();
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .ingredient(ingredient)
                .quantity(100.0)
                .unit("g")
                .build();

        IngredientDto result = recipeIngredientMapper.toDto(recipeIngredient);

        assertEquals(recipeIngredient.getIngredient().getName(), result.getName());
        assertEquals(recipeIngredient.getQuantity(), result.getQuantity());
        assertEquals(recipeIngredient.getUnit(), result.getUnit());
    }
}
