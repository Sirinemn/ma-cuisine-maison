package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.CategoryService;
import fr.sirine.starter.mapper.EntityMapper;
import fr.sirine.starter.user.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class RecipeMapper implements EntityMapper<RecipeDto, Recipe> {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Mappings({
            @Mapping(target = "user", expression = "java(recipeDto.getUserId() != null ? this.userService.findById(recipeDto.getUserId()) : null)"),
            @Mapping(target = "category", expression = "java(recipeDto.getCategoryId() != null ? this.categoryService.findById(recipeDto.getCategoryId()) : null)")
    })
    public abstract Recipe toEntity(RecipeDto recipeDto);

    @Mappings({
            @Mapping(source = "recipe.user.pseudo", target = "userPseudo"),
            @Mapping(source = "recipe.user.id", target = "userId"),
            @Mapping(expression = "java(recipe.getCategory().getId())", target = "categoryId"),
            @Mapping(expression = "java(recipe.getCategory().getName())", target = "categoryName")
    })
    public abstract RecipeDto toDto(Recipe recipe);
}
