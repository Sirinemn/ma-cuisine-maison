package fr.sirine.cuisine.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import fr.sirine.cuisine.category.Category;
import fr.sirine.cuisine.category.CategoryService;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.cuisine.recipe.RecipeDto;
import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.cuisine.recipe.RecipeMapperImpl;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class RecipeMapperTest {

    @InjectMocks
    private RecipeMapperImpl recipeMapper;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    private Recipe recipe;
    private RecipeDto recipeDto;
    private User user;
    private Category category;

    @BeforeEach
    public void setUp() {
        user = User.builder().id(1).pseudo("testuser").build();
        category = Category.builder().id(1).name(RecipeCategory.DESSERTS).build();

        recipe = Recipe.builder()
                .id(1)
                .title("Test Recipe")
                .description("Test Description")
                .cookingTime(30)
                .servings(4)
                .user(user)
                .category(category)
                .build();

        recipeDto = RecipeDto.builder()
                .id(1)
                .title("Test Recipe")
                .description("Test Description")
                .cookingTime(30)
                .servings(4)
                .userId(1)
                .userPseudo("testuser")
                .categoryId(1)
                .categoryName("DESSERTS")
                .build();
    }

    @Test
    public void testToEntity() {
        when(userService.findById(1)).thenReturn(user);
        when(categoryService.findById(1)).thenReturn(category);

        Recipe result = recipeMapper.toEntity(recipeDto);

        assertEquals(recipeDto.getTitle(), result.getTitle());
        assertEquals(recipeDto.getDescription(), result.getDescription());
        assertEquals(recipeDto.getCookingTime(), result.getCookingTime());
        assertEquals(recipeDto.getServings(), result.getServings());
        assertEquals(recipeDto.getUserId(), result.getUser().getId());
        assertEquals(recipeDto.getCategoryId(), result.getCategory().getId());
    }

    @Test
    public void testToDto() {
        RecipeDto result = recipeMapper.toDto(recipe);

        assertEquals(recipe.getTitle(), result.getTitle());
        assertEquals(recipe.getDescription(), result.getDescription());
        assertEquals(recipe.getCookingTime(), result.getCookingTime());
        assertEquals(recipe.getServings(), result.getServings());
        assertEquals(recipe.getUser().getId(), result.getUserId());
        assertEquals(recipe.getCategory().getId(), result.getCategoryId());
    }
}
