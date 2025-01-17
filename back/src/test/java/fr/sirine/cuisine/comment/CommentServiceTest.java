package fr.sirine.cuisine.comment;

import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.starter.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Recipe recipe;
    private Comment comment;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1)
                .pseudo("pseudo")
                .build();
        recipe = Recipe.builder()
                .id(1)
                .build();
        comment = Comment.builder()
                .content("dummy comment")
                .id(1)
                .recipe(recipe)
                .user(user)
                .build();
    }

    @Test
    void saveCommentTest() {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = (Comment) commentService.saveComment(comment);

        assertEquals(comment.getId(), result.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    @Test
    void deleteCommentTest() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1);

        verify(commentRepository, times(1)).delete(comment);
    }
    @Test
    void findByRecipeId() {
        when(commentRepository.findByRecipeId(any(Integer.class))).thenReturn(List.of(comment));

        List<Comment> result = commentService.findByRecipeId(1);

        assertEquals(1, result.size());
        verify(commentRepository, times(1)).findByRecipeId(1);
    }
}
