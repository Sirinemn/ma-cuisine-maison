package fr.sirine.cuisine.recipe;


import fr.sirine.cuisine.exception.ResourceNotFoundException;
import fr.sirine.cuisine.ingredient.IngredientDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }
    public RecipeDto getRecipeById(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        List<IngredientDto> ingredientDtos = recipe.getIngredients().stream()
                .map(ri -> new IngredientDto(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity(), // Récupéré depuis RecipeIngredient
                        ri.getUnit()      // Récupéré depuis RecipeIngredient
                ))
                .collect(Collectors.toList());

        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getCookingTime(),
                recipe.getServings(),
                recipe.getUser().getId(),
                recipe.getUser().getPseudo(),
                recipe.getCategory().getId(),
                recipe.getCategory().getName().name(),
                ingredientDtos,
                recipe.getImage() != null ? recipe.getImage().getImageLocation() : null,
                recipe.getImage() != null ? recipe.getImage().getThumbnailLocation() : null
                );
    }

    public List<RecipeDto> findAll(){
         List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(recipeMapper::toDto).collect(Collectors.toList());
    }

    public List<RecipeDto> findRecipeByCategory(String categoryName) {
        List<Recipe> recipes = recipeRepository.findByCategoryName(categoryName);
        return recipes.stream().map(recipeMapper::toDto).collect(Collectors.toList()); }

    public Set<RecipeDto> getRecipesByUserId(Integer userId){
        Set<Recipe> recipes = recipeRepository.findByUserId(userId);
        return recipes.stream().map(recipeMapper::toDto).collect(Collectors.toSet());
    }

    public void deleteRecipe(Integer id) {
        recipeRepository.deleteById(id);
    }
}
