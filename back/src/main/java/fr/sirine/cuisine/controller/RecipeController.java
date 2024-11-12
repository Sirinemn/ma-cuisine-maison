package fr.sirine.cuisine.controller;

import fr.sirine.cuisine.image.Image;
import fr.sirine.cuisine.image.ImageService;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe.RecipeDto;
import fr.sirine.cuisine.recipe.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public RecipeController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Recipe> createRecipe(
            @RequestPart("recipe") RecipeDto recipeDto,
            @RequestPart("image") MultipartFile imageFile) {

        // Créer la recette en utilisant le service
        Recipe createdRecipe = recipeService.createRecipe(recipeDto);

        // Sauvegarder l'image et l'associer à la recette si elle existe
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.saveImage(imageFile);
            image.setRecipe(createdRecipe);
        }

        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

}
