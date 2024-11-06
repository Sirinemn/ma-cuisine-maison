package fr.sirine.cuisine.recipe_ingredient;

import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.starter.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class RecipeIngredientMapper implements EntityMapper<RecipeIngredient, IngredientDto> {

    @Mapping(source = "recipeIngredient.ingredient.id", target = "ingredientId")
    @Mapping(source = "recipeIngredient.ingredient.name", target = "ingredientName")
    @Mapping(source = "recipeIngredient.quantity", target = "quantity")
    @Mapping(source = "recipeIngredient.unit", target = "unit")
    public abstract IngredientDto toDto(RecipeIngredient recipeIngredient);
}
