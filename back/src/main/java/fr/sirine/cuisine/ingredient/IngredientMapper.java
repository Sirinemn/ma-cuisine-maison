package fr.sirine.cuisine.ingredient;

import fr.sirine.cuisine.recipe.RecipeService;
import fr.sirine.starter.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class IngredientMapper implements EntityMapper<IngredientDto, Ingredient> {


    public abstract Ingredient toEntity(IngredientDto ingredientDto);

    public abstract IngredientDto toDto(Ingredient ingredient);
}
