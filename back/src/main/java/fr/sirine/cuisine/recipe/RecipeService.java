package fr.sirine.cuisine.recipe;

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

    public List<RecipeDto> findAll(){
         List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(recipeMapper::toDto).collect(Collectors.toList());
    }

    public List<RecipeDto> findRecipeByCategory(String categoryName) {
        List<Recipe> recipes = recipeRepository.findByCategoryName(categoryName);
        return recipes.stream().map(recipeMapper::toDto).collect(Collectors.toList()); }

    public RecipeDto findById(Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        return recipeMapper.toDto(recipe);
    }

    public Set<RecipeDto> getRecipesByUserId(Integer userId){
        Set<Recipe> recipes = recipeRepository.findByUserId(userId);
        return recipes.stream().map(recipeMapper::toDto).collect(Collectors.toSet());
    }

    public void deleteRecipe(Integer id) {
        recipeRepository.deleteById(id);
    }
}
