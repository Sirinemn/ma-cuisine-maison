package fr.sirine.cuisine.mapper;

import fr.sirine.cuisine.comment.Comment;
import fr.sirine.cuisine.comment.CommentDto;
import fr.sirine.cuisine.comment.CommentMapper;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe.RecipeService;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper;

    @Mock
    private UserService userService;

    @Mock
    private RecipeService recipeService;

    private User user;
    private Comment comment;
    private CommentDto commentDto;
    private Recipe recipe;

    @BeforeEach
    void setUp(){
        user = User.builder().id(2).pseudo("testuser").build();
        recipe = Recipe.builder().id(3).title("Test Recipe").build();

        comment = Comment.builder()
                .id(1)
                .content("Content")
                .recipe(recipe)
                .user(user)
                .build();
        commentDto = CommentDto.builder()
                .content("Content")
                .userId(2)
                .recipeId(3)
                .userPseudo("testuser")
                .build();
    }

    @Test
    void testToEntity(){
        when(userService.findById(2)).thenReturn(user);
        when(recipeService.findById(3)).thenReturn(recipe);

        Comment result = commentMapper.toEntity(commentDto);

        assertEquals(commentDto.getId(), result.getId());
        assertEquals(commentDto.getRecipeId(), result.getRecipe().getId());
        assertEquals(commentDto.getContent(), result.getContent());
        assertEquals(commentDto.getUserId(), result.getUser().getId());

    }

    @Test
    void testToDto(){
        CommentDto result = commentMapper.toDto(comment);

        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getRecipe().getId(), result.getRecipeId());
        assertEquals(comment.getContent(), result.getContent());
        assertEquals(comment.getUser().getId(), result.getUserId());
    }
}
