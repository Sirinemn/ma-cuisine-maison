package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.ingredient.IngredientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /*@PostMapping("/create")
    public ResponseEntity<RecipeDto> createRecipe(
            @RequestParam("userId") Integer userId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("ingredients") List<IngredientDto> ingredientDtos,
            @RequestPart("file") MultipartFile file,
            @RequestBody RecipeDto recipeDto) {

        RecipeDto createdRecipe = recipeService.createRecipe(userId, categoryName, ingredientDtos, file, recipeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }*/
}

