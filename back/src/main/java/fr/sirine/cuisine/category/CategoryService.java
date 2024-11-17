package fr.sirine.cuisine.category;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Integer id){
        return this.categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Category findByName(String categoryName) {
        RecipeCategory recipeCategory;
        try {
            recipeCategory = RecipeCategory.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Catégorie de recette inconnue : " + categoryName);
        }
        // Chercher la catégorie dans la base de données
        Optional<Category> existingCategory = categoryRepository.findByName(recipeCategory);
        if (existingCategory.isPresent()) {
            return existingCategory.get();
        } else {
            // Créer et enregistrer une nouvelle catégorie si elle n'existe pas
            Category newCategory = new Category();
            newCategory.setName(recipeCategory);
            return categoryRepository.save(newCategory);
        }
    }
}
