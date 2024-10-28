package fr.sirine.cuisine.category;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Category findByName(String name){
        return this.categoryRepository.findByName(name);
    }
    public Category findById(Integer id){
        return this.categoryRepository.findById(id).orElse(null);
    }
}
