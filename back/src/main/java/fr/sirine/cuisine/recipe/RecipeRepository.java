package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByCategoryName(RecipeCategory categoryName);

    List<Recipe> findByUserId(Integer userId);
}
