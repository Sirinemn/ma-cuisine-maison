package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.CategoryService;
import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.cuisine.image.ImageService;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import fr.sirine.starter.mapper.EntityMapper;
import fr.sirine.starter.user.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", imports = {RecipeCategory.class})
public abstract class RecipeMapper implements EntityMapper<RecipeDto, Recipe> {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ImageService imageService;

    @Mappings({
            @Mapping(target = "user", expression = "java(recipeDto.getUserId() != null ? userService.findById(recipeDto.getUserId()) : null)"),
            @Mapping(target = "image", expression = "java(recipeDto.getImageId() != null ? imageService.findById(recipeDto.getImageId()) : null)"),
            @Mapping(target = "category", expression = "java(recipeDto.getCategoryName() != null ? categoryService.findByName(recipeDto.getCategoryName()) : null)"),
            @Mapping(target = "ingredients", ignore = true) // gérer les ingrédients séparément si nécessaire
    })
    public abstract Recipe toEntity(RecipeDto recipeDto);

    @Mappings({
            @Mapping(source = "recipe.user.pseudo", target = "userPseudo"),
            @Mapping(source = "recipe.user.id", target = "userId"),
            @Mapping(source = "recipe.image.id", target = "imageId"),
            @Mapping(expression = "java(recipe.getCategory() != null ? recipe.getCategory().getName().name() : null)", target = "categoryName"),
            @Mapping(target = "ingredients", ignore = true) // Gérez les ingrédients séparément si besoin
    })
    public abstract RecipeDto toDto(Recipe recipe);

    //  Méthode utilitaire pour convertir la liste des ingrédients si nécessaire
    protected List<IngredientDto> mapIngredients(List<RecipeIngredient> recipeIngredients) {
        return recipeIngredients.stream()
                .map(ri -> new IngredientDto(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity(),
                        ri.getUnit()
                ))
                .collect(Collectors.toList());
    }
}
