package fr.sirine.cuisine.ingredient;

import fr.sirine.cuisine.exception.ExternalApiException;
import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import  org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Value("${app.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final IngredientRepository ingredientRepository;

    public IngredientService(WebClient webClient, IngredientRepository ingredientRepository) {
        this.webClient = webClient;
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
    @Transactional
    public List<Ingredient> processIngredients(List<IngredientRequest> ingredientRequests) {
        return ingredientRequests.stream()
                .map(ingredientRequest -> {
                    // Vérifier si l'ingrédient existe déjà dans la base de données
                    Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredientRequest.getIngredientName());
                    if (existingIngredient.isPresent()) {
                        // Retourner l'ingrédient existant
                        return existingIngredient.get();

                    } else {
                        // Créer un nouvel ingrédient s'il n'existe pas
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredientRequest.getIngredientName());
                        return ingredientRepository.save(newIngredient);
                    }
                })
                .collect(Collectors.toList());
    }
    public Ingredient findOrCreateIngredient(String name) {
        return ingredientRepository.findByName(name)
                .orElseGet(() -> {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(name);
                    return ingredientRepository.save(newIngredient);
                });
    }
    public void deleteIngredient(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
    }

    public Ingredient createIngredient(IngredientRequest ingredientRequest) {
        return ingredientRepository.findByName(ingredientRequest.getIngredientName())
                .orElseGet(() -> {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingredientRequest.getIngredientName());
                    return ingredientRepository.save(newIngredient);
                });
    }
}
