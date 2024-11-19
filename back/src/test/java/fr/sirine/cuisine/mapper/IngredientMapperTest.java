package fr.sirine.cuisine.mapper;

import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientMapper;
import fr.sirine.cuisine.recipe.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class IngredientMapperTest {

    @InjectMocks
    private IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);

    private IngredientDto ingredientDto;

    @BeforeEach
    public void setUp(){
        Recipe recipe = Recipe.builder().id(1).title("Test Recipe").build();

        Ingredient ingredient = Ingredient.builder()
                .id(1)
                .name("Name")
                .build();

        ingredientDto = IngredientDto.builder()
                .name("Name")
                .build();
    }

    @Test
    void testToEntity(){

        Ingredient result = ingredientMapper.toEntity(ingredientDto);

        assertEquals(ingredientDto.getId(), result.getId());
        assertEquals(ingredientDto.getName(), result.getName());

    }

}
