package fr.sirine.cuisine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sirine.MaCuisineMaison;
import fr.sirine.cuisine.image.Image;
import fr.sirine.cuisine.image.ImageService;
import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientService;
import fr.sirine.cuisine.payload.IngredientRequest;
import fr.sirine.cuisine.payload.RecipeRequest;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe.RecipeDto;
import fr.sirine.cuisine.recipe.RecipeService;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredientService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = MaCuisineMaison.class)
public class RecipeControllerIT {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    RecipeService recipeService;
    @MockBean
    ImageService imageService;
    @MockBean
    IngredientService ingredientService;
    @MockBean
    RecipeIngredientService recipeIngredientService;

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void createRecipeTest() throws Exception {
        // Préparation des données
        IngredientRequest ingredientRequest = IngredientRequest.builder()
                .name("Tomate")
                .unit("gr")
                .quantity(100.0)
                .build();
        RecipeRequest recipeRequest = RecipeRequest.builder()
                .categoryName("DESSERTS")
                .cookingTime(15)
                .description("description")
                .servings(5)
                .title("Pasta")
                .userId(1)
                .userPseudo("Pseudo")
                .build();
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "some image".getBytes());

        // Sérialisation des objets JSON
        ObjectMapper objectMapper = new ObjectMapper();
        MockMultipartFile recipeRequestPart = new MockMultipartFile(
                "recipeRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(recipeRequest)
        );
        MockMultipartFile ingredientRequestsPart = new MockMultipartFile(
                "ingredientRequests",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(Collections.singletonList(ingredientRequest))
        );

        // Mock des services
        when(ingredientService.processIngredients(anyList())).thenReturn(List.of(new Ingredient()));
        when(imageService.saveImage(any(MultipartFile.class))).thenReturn(new Image());
        when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(new Recipe());
        doNothing().when(recipeIngredientService).createAndSaveRecipeIngredients(anyList(), any(), anyList());

        // Envoi de la requête
        mockMvc.perform(multipart("/recipes/add")
                        .file(file)
                        .file(recipeRequestPart)
                        .file(ingredientRequestsPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recipe added with success!"));
    }

}
