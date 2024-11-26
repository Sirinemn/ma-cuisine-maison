package fr.sirine.cuisine.controller;

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
import fr.sirine.cuisine.recipe.RecipeMapper;
import fr.sirine.cuisine.recipe.RecipeService;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientMapper;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    private final RecipeService recipeService;
    private final ImageService imageService;
    private final IngredientService ingredientService;
    private final RecipeIngredientService recipeIngredientService;

    public RecipeController(RecipeService recipeService, ImageService imageService, IngredientService ingredientService, RecipeIngredientService recipeIngredientService, RecipeMapper recipeMapper, RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeService = recipeService;
        this.imageService = imageService;
        this.ingredientService = ingredientService;
        this.recipeIngredientService = recipeIngredientService;
    }

    @Operation(summary = "Create a new recipe", description = "Creates a new recipe with ingredients and image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recipe added with success!"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value= "/add",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageResponse> createRecipe(
            @Valid @RequestPart RecipeRequest recipeRequest,
            @Valid @RequestPart List<@Valid IngredientRequest> ingredientRequests,
            @RequestPart(value = "imageFile",required = false) MultipartFile imageFile) {
        try {

        // Process ingredients
        List<Ingredient> ingredients = ingredientService.processIngredients(ingredientRequests);

        // Create Recipe object from RecipeRequest
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setTitle(recipeRequest.getTitle());
        recipeDto.setDescription(recipeRequest.getDescription());
        recipeDto.setCookingTime(recipeRequest.getCookingTime());
        recipeDto.setServings(recipeRequest.getServings());
        recipeDto.setUserId(recipeRequest.getUserId());
        recipeDto.setUserPseudo(recipeRequest.getUserPseudo());
        recipeDto.setCategoryName(recipeRequest.getCategoryName());
        recipeDto.setIngredients(ingredientRequests.stream().map(req -> {
            IngredientDto dto = new IngredientDto();
            dto.setName(req.getName());
            dto.setQuantity(req.getQuantity());
            dto.setUnit(req.getUnit());
            return dto;
        }).collect(Collectors.toList()));

        // Process image and set URLs
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.saveImage(imageFile);
            recipeDto.setImageThumbUrl(image.getThumbnailLocation());
            recipeDto.setImageUrl(image.getImageLocation());
            recipeDto.setImageId(image.getId());
        }

        // Save the recipe
        Recipe recipe = recipeService.createRecipe(recipeDto);

        // Create and save RecipeIngredients
        recipeIngredientService.createAndSaveRecipeIngredients(ingredients, recipe, ingredientRequests);

        MessageResponse messageResponse = new MessageResponse("Recipe added with success!");
        return new ResponseEntity<>( messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("An error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
