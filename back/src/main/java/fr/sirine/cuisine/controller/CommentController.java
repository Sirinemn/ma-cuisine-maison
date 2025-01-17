package fr.sirine.cuisine.controller;

import fr.sirine.cuisine.comment.Comment;
import fr.sirine.cuisine.comment.CommentDto;
import fr.sirine.cuisine.comment.CommentMapper;
import fr.sirine.cuisine.comment.CommentService;
import fr.sirine.cuisine.payload.CommentResponse;
import fr.sirine.cuisine.payload.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }
    @PostMapping()
    public ResponseEntity<MessageResponse> create(@RequestBody CommentDto commentDto) {
        this.commentService.saveComment(this.commentMapper.toEntity(commentDto));
        MessageResponse messageResponse = new MessageResponse("Comment added with success!");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentsByRecipe(@PathVariable String id) {
        List<Comment> comments = commentService.findByRecipeId(Integer.parseInt(id));
        List<CommentDto> commentDtos = new ArrayList<>();
        commentDtos = comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
        CommentResponse commentResponse = new CommentResponse(commentDtos);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }
}
