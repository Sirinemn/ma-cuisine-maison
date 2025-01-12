package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.Category;
import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.cuisine.ingredient.IngredientDto;
import fr.sirine.cuisine.ingredient.IngredientService;
import fr.sirine.starter.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    RecipeService recipeService;
    @Mock
    RecipeMapper recipeMapper;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    IngredientService ingredientService;

    private Recipe recipe;
    private RecipeDto recipeDto;
    private Ingredient ingredient;
    private RecipeCategory recipeCategory;

    @BeforeEach
    void setUp() {
        recipeCategory = RecipeCategory.valueOf("ENTREES");

        Category category = Category.builder()
                .name(recipeCategory)
                .build();
        ingredient = Ingredient.builder()
                .name("tomate")
                .build();

        IngredientDto ingredientDto = IngredientDto.builder()
                .unit("gr")
                .quantity(100.0)
                .name("tomate")
                .build();
        recipe = Recipe.builder()
                .id(1)
                .title("title")
                .category(category)
                .build();
        recipeDto = RecipeDto.builder()
                .id(1)
                .title("title")
                .ingredients(List.of(ingredientDto))
                .categoryName("ENTREES")
                .userId(1)
                .userPseudo("pseudo")
                .build();
    }
    @Test
    void getAllRecipesTest() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDto);

        List<RecipeDto> result = recipeService.getAllRecipes();
        System.out.println(result);
        assertEquals(result.get(0).getId(), recipe.getId());
    }

    @Test
    void getRecipeByIdTest() {
        Integer id = 1;

        when(recipeRepository.findById(id)).thenReturn(Optional.ofNullable(recipe));

        Recipe result = recipeService.getRecipeById(id);

        assertEquals("title", result.getTitle());
    }

    @Test public void testCreateRecipe() {

        when(recipeMapper.toEntity(any(RecipeDto.class))).thenReturn(recipe);
        when(ingredientService.findOrCreateIngredient(anyString())).thenReturn(ingredient);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        Recipe result = recipeService.createRecipe(recipeDto);

        assertNotNull(result);
        assertEquals(recipe.getId(), result.getId());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
    @Test
    void getRecipesByCategoryTest() {

        when(recipeMapper.toDto(recipe)).thenReturn(recipeDto);
        when(recipeRepository.findByCategoryName(recipeCategory)).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.getRecipesByCategory(recipeCategory);

        assertEquals(1, result.get(0).getId());
        assertEquals("ENTREES", result.get(0).getCategoryName());
        assertEquals("title", result.get(0).getTitle());
    }
    @Test
    void getRecipesByUserTest() {
        Integer userId = 1;
        User user = User.builder()
                .id(userId)
                .pseudo("pseudo")
                .build();
        when(recipeRepository.findByUserId(1)).thenReturn(List.of(recipe));
        when(recipeMapper.toDto(recipe)).thenReturn(recipeDto);

        List<RecipeDto> result = recipeService.getRecipesByUser(userId);

        assertEquals(recipeDto.getUserId(), result.get(0).getUserId());
        assertEquals(recipeDto.getUserPseudo(), result.get(0).getUserPseudo());
    }

    @Test
    void deleteRecipeTest() {
        when(recipeRepository.findById(1)).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(1);

        verify(recipeRepository, times(1)).delete(recipe);
    }
}
