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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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

    @BeforeEach
    public void setUp() {
        IngredientRequest ingredientRequest = IngredientRequest.builder()
                .name("Tomate")
                .unit("gr")
                .quantity(100.0)
                .build();
        Ingredient ingredient = Ingredient.builder()
                .name("Tomate")
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
    }
    @Test
    void createRecipeTest() throws Exception {
        IngredientRequest ingredientRequest = IngredientRequest.builder()
                .name("Tomate")
                .unit("gr")
                .quantity(100.0)
                .build();
        Ingredient ingredient = Ingredient.builder()
                .name("Tomate")
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

        when(ingredientService.processIngredients(anyList())).thenReturn(List.of(ingredient));
        when(imageService.saveImage(any(MultipartFile.class))).thenReturn(any(Image.class));
        when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(any(Recipe.class));
        doNothing().when(recipeIngredientService).createAndSaveRecipeIngredients(anyList(), any(), anyList());

        mockMvc.perform(multipart("/add")
                .file(file)
                .param("recipeRequest", new ObjectMapper().writeValueAsString(recipeRequest))
                .param("ingredientRequests", new ObjectMapper().writeValueAsString(Collections.singletonList(ingredientRequest)))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Recipe added with success!"));
    }

}
