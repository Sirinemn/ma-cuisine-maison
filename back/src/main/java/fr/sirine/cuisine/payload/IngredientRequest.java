package fr.sirine.cuisine.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IngredientRequest {

    private String ingredientName;
    private Double quantity;
    private String unit;
}
