package fr.sirine.cuisine.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Integer id;

    @NotNull
    @Size(max = 250)
    private String content;

    @NotNull
    private Integer userId;
    private String userPseudo;

    @NotNull
    private Integer recipeId;

}
