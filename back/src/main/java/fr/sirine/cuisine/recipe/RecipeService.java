package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.cuisine.exception.ResourceNotFoundException;
import fr.sirine.cuisine.image.Image;
import fr.sirine.cuisine.image.ImageService;
import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientMapper;
import fr.sirine.cuisine.ingredient.IngredientService;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class RecipeService {
    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientService ingredientService;
    private final ImageService imageService;


    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, IngredientMapper ingredientMapper, IngredientService ingredientService, ImageService imageService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.ingredientService = ingredientService;
        this.imageService = imageService;
    }

    @Transactional
    public Recipe createRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeMapper.toEntity(recipeDto);

        List<RecipeIngredient> ingredients = recipeDto.getIngredients().stream()
                .map(ingredientDto -> {
                    Ingredient ingredient = ingredientService.findOrCreateIngredient(ingredientDto.getName());

                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setQuantity(ingredientDto.getQuantity());
                    recipeIngredient.setUnit(ingredientDto.getUnit());
                    recipeIngredient.setRecipe(recipe);
                    return recipeIngredient;
                })
                .collect(Collectors.toList());

        recipe.setIngredients(ingredients);

        return recipeRepository.save(recipe);
    }

    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<RecipeDto> getRecipesByCategory(RecipeCategory categoryName) {
        return recipeRepository.findByCategoryName(categoryName)
                .stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<RecipeDto> getRecipesByUser(Integer userId) {
        return recipeRepository.findByUserId(userId)
                .stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteRecipe(Integer id) {
        recipeRepository.findById(id).ifPresent(recipeRepository::delete);
    }

    public Recipe getRecipeById(Integer recipeId) {
        return recipeRepository.findById(recipeId).orElse(null);
    }
    public RecipeDto getRecipeDto(Integer id) {
        return recipeMapper.toDto(recipeRepository.findById(id).orElse(null));
    }
}
