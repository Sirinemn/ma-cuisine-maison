package fr.sirine.cuisine.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByCategoryName(String categoryName);

    Set<Recipe> findByUserId(Integer userId);
}
