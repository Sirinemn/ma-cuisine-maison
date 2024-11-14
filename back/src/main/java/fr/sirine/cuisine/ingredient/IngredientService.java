package fr.sirine.cuisine.ingredient;

import fr.sirine.cuisine.exception.ExternalApiException;
import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import  org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Value("${app.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;

    public IngredientService(WebClient webClient, IngredientMapper ingredientMapper, IngredientRepository ingredientRepository) {
        this.webClient = webClient;
        this.ingredientMapper = ingredientMapper;
        this.ingredientRepository = ingredientRepository;
    }

    public List<String> autocompleteIngredients(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ingredients/autocomplete")
                        .queryParam("query", query)
                        .queryParam("number", 5)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ExternalApiException("Erreur côté client : requête invalide", HttpStatus.BAD_REQUEST))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ExternalApiException("Erreur côté serveur : service temporairement indisponible", HttpStatus.SERVICE_UNAVAILABLE))
                )
                .bodyToFlux(IngredientDto.class)
                .map(IngredientDto::getName)
                .collectList()
                .block();
    }

    public List<Ingredient> processIngredients(List<IngredientRequest> ingredientRequests) {
        List<Ingredient> ingredients = new ArrayList<>();

        for (IngredientRequest ingredientRequest : ingredientRequests) {
            // Vérifier si l'ingrédient existe dans la base de données
            Ingredient ingredient = ingredientRepository.findByName(ingredientRequest.getName())
                    .orElseGet(() -> {
                        // Créer un nouvel ingrédient s'il n'existe pas
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredientRequest.getName());
                        return ingredientRepository.save(newIngredient);
                    });

            ingredients.add(ingredient);
        }

        return ingredients;
    }

    public void save(Ingredient ingredient) {
        this.ingredientRepository.save(ingredient);
    }
}
