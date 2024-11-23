package fr.sirine.cuisine.category;

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
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    void findByIdTest(){
        Integer id = 1;
        categoryService.findById(id);
        verify(categoryRepository, times(1)).findById(id);
    }
    @Test
    void testFindByNameExistingCategory() {
        String categoryName = "DESSERTS";
        RecipeCategory recipeCategory = RecipeCategory.DESSERTS;
        Category category = new Category();
        category.setName(recipeCategory);
        when(categoryRepository.findByName(recipeCategory)).thenReturn(Optional.of(category));
        Category result = categoryService.findByName(categoryName);
        assertNotNull(result);
        assertEquals(recipeCategory, result.getName());
    }
    @Test
    void getAllCategoriesTest() {
        RecipeCategory recipeCategory = RecipeCategory.ENTREES;
        Category category = Category.builder()
                .name(recipeCategory)
                .build();
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<String> result = categoryService.getAllCategories();

        verify(categoryRepository, times(1)).findAll();
        assertEquals(result.get(0), "ENTREES");
    }
}
