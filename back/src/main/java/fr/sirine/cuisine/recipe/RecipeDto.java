package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.cuisine.ingredient.Ingredient;
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

    private List<Ingredient> ingredients; // Utilisation de DTO pour les ingrédients
    @NotNull
    private Integer userId;
    private String userPseudo;

    @NotNull
    private Integer categoryId;
    private RecipeCategory categoryName;

    private String imageUrl;
    private String thumbnailUrl;
}

