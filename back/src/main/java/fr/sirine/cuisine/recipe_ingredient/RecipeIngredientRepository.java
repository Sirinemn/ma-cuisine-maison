package fr.sirine.cuisine.recipe_ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {
    boolean existsByIngredientId(Integer ingredientId);
}
