package fr.sirine.cuisine.comment;

import jakarta.transaction.Transactional;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
