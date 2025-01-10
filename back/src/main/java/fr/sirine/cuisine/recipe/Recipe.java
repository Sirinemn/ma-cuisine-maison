package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.Category;
import fr.sirine.cuisine.image.Image;
import fr.sirine.cuisine.recipe_ingredient.RecipeIngredient;
import fr.sirine.starter.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="_recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private int cookingTime;
    private int servings;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();


}
