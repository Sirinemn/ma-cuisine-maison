package fr.sirine.cuisine.ingredient;

import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.ingredient.IngredientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @InjectMocks
    IngredientService ingredientService;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    WebClient webClient;

    @Test
    void autocompleteIngredientsTest(){
        String query = "tomate";
        IngredientDto ingredientDto = IngredientDto.builder()
                .name("tomato")
                .build();

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux((IngredientDto.class))).thenReturn(Mono.just(ingredientDto).flux());

        List<String> result = ingredientService.autocompleteIngredients(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tomato", result.get(0));
    }

    @Test
    void testProcessIngredients() {
        IngredientRequest request = IngredientRequest.builder()
                .ingredientName("Tomate")
                .build();
        Ingredient existingIngredient = new Ingredient();
        existingIngredient.setName("Tomato");

        when(ingredientRepository.findByName(any(String.class))).thenReturn(Optional.of(existingIngredient));

        List<Ingredient> result = ingredientService.processIngredients(Collections.singletonList(request));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tomato", result.get(0).getName());
    }

    @Test
    void finOrCreateIngredientTest(){
        Ingredient ingredient = Ingredient.builder()
                .name("Sugar")
                .build();
        String name = "Sugar";

        when(ingredientRepository.findByName(any(String.class))).thenReturn(Optional.ofNullable(ingredient));

        Ingredient result = ingredientService.findOrCreateIngredient(name);

        assertNotNull(result);
        assertEquals("Sugar", result.getName());
    }
}
