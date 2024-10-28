package fr.sirine.cuisine.ingredient;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDto {
    private Integer id;
    private String name;
    private String quantity;
    @NotNull
    private Integer recipeId;
}

