package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.Category;
import fr.sirine.cuisine.category.CategoryService;
import fr.sirine.cuisine.image.Image;
import fr.sirine.cuisine.image.ImageService;
import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientMapper;
import fr.sirine.cuisine.ingredient.IngredientService;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final UserService userService;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, ImageService imageService, CategoryService categoryService, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.imageService = imageService;
        this.categoryService = categoryService;

        this.userService = userService;
    }

    /*public RecipeDto createRecipe(Integer userId, String categoryName, List<IngredientDto> ingredientDtos, MultipartFile file, RecipeDto recipeDto) {
        // Récupération de la catégorie par nom
        Category category = categoryService.findByName(categoryName);

        // Récupération de l'utilisateur par nom d'utilisateur
        User user = userService.findById(userId);

        // Création de l'entité recette
        Recipe recipe = Recipe.builder()
                .title(recipeDto.getTitle())
                .description(recipeDto.getDescription())
                .cookingTime(recipeDto.getCookingTime())
                .servings(recipeDto.getServings())
                .category(category)
                .user(user)
                .build();

        // Sauvegarde de l'image
        Image image = imageService.saveImage(file, recipe);
        recipe.setImageUrl(image.getImageLocation());
        recipe.setThumbnailUrl(image.getThumbnailLocation());

        // Transformation des IngredientDto en entités Ingredient et sauvegarde
        List<Ingredient> ingredients = this.ingredientMapper.toEntity(ingredientDtos);
        ingredients.stream().map(ingredient -> {
           this.ingredientService.save(ingredient);
            return null;
        });
        recipe.setIngredients(ingredients);  // Ajoute les ingrédients à la recette

        // Sauvegarde de la recette avec les ingrédients et l'image
        recipeRepository.save(recipe);

        // Retourne la recette créée en Dto
        return recipeMapper.toDto(recipe);
    }*/

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
