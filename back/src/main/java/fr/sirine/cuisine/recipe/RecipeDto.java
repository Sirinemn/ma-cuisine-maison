package fr.sirine.cuisine.recipe;


import fr.sirine.cuisine.ingredient.IngredientDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {

    private Integer id;
    private String title;
    private String description;
    private int cookingTime;
    private int servings;

    @NotNull
    private Integer userId;
    private String userPseudo;

    @NotNull
    private String categoryName;

    private List<IngredientDto> ingredients;

    @NotNull
    private String imageUrl;
    private String imageThumbUrl;

}

