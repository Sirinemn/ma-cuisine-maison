package fr.sirine.cuisine.comment;

import fr.sirine.cuisine.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    public List<Comment> findByRecipeId(Integer recipeId) {
        return commentRepository.findByRecipeId(recipeId);
    }
    public void deleteComment(Integer commentId) {
        commentRepository.findById(commentId).ifPresent(commentRepository::delete);
    }
    public Comment updateComment(Integer commentId, String newContent) {
        return commentRepository.findById(commentId).map(
                comment -> {
                    comment.setContent(newContent);
                    comment.setLastModifiedDate(LocalDateTime.now());
                    return commentRepository.save(comment);
                }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id"));
    }
}
