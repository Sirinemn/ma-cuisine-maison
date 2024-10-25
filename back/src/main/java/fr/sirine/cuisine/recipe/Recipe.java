package fr.sirine.cuisine.recipe;

import fr.sirine.cuisine.category.Category;
import fr.sirine.cuisine.ingredient.Ingredient;
import fr.sirine.starter.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
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

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Ingredient> ingredients;

    @ManyToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name="user_id")
    private User user;

    private String imageUrl; // Chemin vers l'image en taille réelle
    private String thumbnailUrl; // Chemin vers la miniature

}
