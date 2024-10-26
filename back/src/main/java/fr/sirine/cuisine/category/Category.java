package fr.sirine.cuisine.category;

import fr.sirine.cuisine.recipe.Recipe;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RecipeCategory name;

    @OneToMany(mappedBy = "category")
    private List<Recipe> recipes;
}
