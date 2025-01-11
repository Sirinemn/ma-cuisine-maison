package fr.sirine.cuisine.controller;

import fr.sirine.cuisine.category.CategoryService;
import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.cuisine.exception.ImageProcessingException;
import fr.sirine.cuisine.exception.ResourceNotFoundException;
import fr.sirine.cuisine.image.Image;
import fr.sirine.cuisine.image.ImageService;
import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientService;
import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.payload.MessageResponse;
import fr.sirine.cuisine.payload.RecipeRequest;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe.RecipeDto;
import fr.sirine.cuisine.recipe.RecipeService;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipe Controller", description = "Recipe Management")
public class RecipeController {
    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final RecipeService recipeService;
    private final ImageService imageService;
    private final IngredientService ingredientService;
    private final CategoryService categoryService;
    private final RecipeIngredientService recipeIngredientService;

    public RecipeController(RecipeService recipeService, ImageService imageService, IngredientService ingredientService, CategoryService categoryService, RecipeIngredientService recipeIngredientService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
        this.ingredientService = ingredientService;
        this.categoryService = categoryService;
        this.recipeIngredientService = recipeIngredientService;
    }
    @Operation(summary = "Get all recipes", description = "Retrieve a list of all recipes")
    @GetMapping(value = "/recipes-list")
    public  List<RecipeDto> getAllRecipes(){
        return recipeService.getAllRecipes();
    }
    @Operation(summary = "Get recipe by ID", description = "Retrieve a recipe by their ID")
    @GetMapping(value = "/recipe/{id}")
    public RecipeDto getRecipeDtoById(@PathVariable Integer id) {
        return recipeService.getRecipeDto(id);
    }
    @Operation(summary = "Delete recipe by ID", description = "Delete a recipe by their ID")
    @DeleteMapping(value = "/recipe/{id}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable Integer id) {
        try {
            // First get the recipe to access its image
            Recipe recipe = recipeService.getRecipeById(id);

            // Store image info before deletion if it exists
            Image image = recipe.getImage();

            // If there was an image, delete it after recipe is deleted
            if (image != null) {
                log.info("Attempting to delete image with ID: {}", image.getId());
                imageService.deleteImage(image.getId());
                log.info("Image deleted successfully before recipe deletion");
            }
            
            List<RecipeIngredient> recipeIngredients = recipe.getIngredients();
            recipeService.deleteRecipe(id);
            // Supprimer chaque ingrédient inutilisé
            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                Ingredient ingredient = recipeIngredient.getIngredient();
                // If the ingredient is not shared with other recipes, delete it
                if (!recipeIngredientService.isShared(recipeIngredient)) {
                    ingredientService.deleteIngredient(ingredient);
                }
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            log.error("Recipe not found for ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error during recipe deletion: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get recipe by category", description = "Retrieve a recipe by their category")
    @GetMapping(value = "/category")
    public List<RecipeDto> getRecipeDtoByCategory(@RequestParam String categoryName) {
        RecipeCategory category = categoryService.convertToRecipeCategory(categoryName);
        return recipeService.getRecipesByCategory(category);
    }
    @Operation(summary = "Get recipe by user", description = "Retrieve a recipe by their user")
    @GetMapping(value = "/user")
    public List<RecipeDto> getRecipeDtoByUser(@RequestParam String userId) {
        return recipeService.getRecipesByUser(Integer.parseInt(userId));
    }

    @Operation(summary = "Create a new recipe", description = "Creates a new recipe with ingredients and image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recipe added with success!"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageResponse> createRecipe(
            @Valid @RequestPart("recipeRequest") RecipeRequest recipeRequest,
            @Valid @RequestPart("ingredientRequests") List<IngredientRequest> ingredientRequests,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            log.info("Receiving recipe creation request for: {}", recipeRequest.getTitle());

            // Validate image if present
            if (imageFile != null && !imageFile.isEmpty()) {
                if (!imageFile.getContentType().startsWith("image/")) {
                    return new ResponseEntity<>(
                            new MessageResponse("Invalid file type. Only images are allowed."),
                            HttpStatus.UNSUPPORTED_MEDIA_TYPE
                    );
                }
            }

            // Process ingredients
            List<Ingredient> ingredients = ingredientService.processIngredients(ingredientRequests);

            // Create Recipe DTO
            RecipeDto recipeDto = RecipeDto.builder()
                    .title(recipeRequest.getTitle())
                    .description(recipeRequest.getDescription())
                    .cookingTime(recipeRequest.getCookingTime())
                    .servings(recipeRequest.getServings())
                    .userId(recipeRequest.getUserId())
                    .userPseudo(recipeRequest.getUserPseudo())
                    .categoryName(recipeRequest.getCategoryName())
                    .ingredients(ingredientRequests.stream()
                            .map(req -> IngredientDto.builder()
                                    .name(req.getIngredientName())
                                    .quantity(req.getQuantity())
                                    .unit(req.getUnit())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            // Process image if present
            Image image = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    image = imageService.saveImage(imageFile);
                    recipeDto.setImageThumbName(image.getThumbnailName());
                    recipeDto.setImageName(image.getImageName());
                    recipeDto.setImageId(image.getId());
                } catch (ImageProcessingException e) {
                    log.error("Error processing image: ", e);
                    return new ResponseEntity<>(
                            new MessageResponse("Error processing image: " + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    );
                }
            }

            // Save recipe
            Recipe recipe = recipeService.createRecipe(recipeDto);

            // Update image with recipe reference if exists
            if (image != null) {
                image.setRecipe(recipe);
                imageService.updateImage(image);
            }

            return new ResponseEntity<>(
                    new MessageResponse("Recipe added successfully!"),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            log.error("Error creating recipe: ", e);
            return new ResponseEntity<>(
                    new MessageResponse("Error creating recipe: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
