package fr.sirine.cuisine.comment;

import fr.sirine.cuisine.recipe.RecipeService;
import fr.sirine.starter.mapper.EntityMapper;
import fr.sirine.starter.user.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class CommentMapper implements EntityMapper<CommentDto, Comment> {

    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;

    @Mappings({
            @Mapping(target = "user", expression = "java(commentDto.getUserId() != null ? this.userService.findById(commentDto.getUserId()) : null)"),
            @Mapping(target = "recipe", expression = "java(commentDto.getRecipeId() != null ? this.recipeService.findById(commentDto.getRecipeId()) : null)")
    })
    public abstract Comment toEntity(CommentDto commentDto);

    @Mappings({
            @Mapping(source = "comment.user.pseudo", target = "userPseudo"),
            @Mapping(source = "comment.user.id", target = "userId"),
            @Mapping(expression = "java(comment.getRecipe().getId())", target = "recipeId")
    })
    public abstract CommentDto toDto(Comment comment);

}
