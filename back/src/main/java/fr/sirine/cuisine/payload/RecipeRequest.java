package fr.sirine.cuisine.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecipeRequest {

    private String title;
    private String description;
    @NotNull(message = "Cooking time are required")
    private Integer cookingTime;
    @NotNull(message = "Servings are required")
    private Integer servings;

    @NotNull
    private Integer userId;
    private String userPseudo;

    @NotNull
    private String categoryName;
}
