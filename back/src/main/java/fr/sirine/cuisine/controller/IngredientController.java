package fr.sirine.cuisine.controller;

import fr.sirine.cuisine.ingredient.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // Endpoint pour l'autocomplétion d'ingrédients
    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocompleteIngredients(@RequestParam String query) {
        List<String> ingredientNames = ingredientService.autocompleteIngredients(query);
        return ResponseEntity.ok(ingredientNames);
    }

}
