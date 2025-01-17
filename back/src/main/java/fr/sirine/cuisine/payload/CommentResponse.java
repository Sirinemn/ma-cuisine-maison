package fr.sirine.cuisine.payload;

import fr.sirine.cuisine.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    List<CommentDto> comments;

}
