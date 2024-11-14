package fr.sirine.cuisine.ingredient;

import fr.sirine.starter.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class IngredientMapper implements EntityMapper<IngredientDto, Ingredient> {

    public abstract Ingredient toEntity(IngredientDto ingredientDto);

}
