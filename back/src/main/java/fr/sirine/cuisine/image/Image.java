package fr.sirine.cuisine.image;

import fr.sirine.cuisine.recipe.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "_image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @NotNull
    private String imageName; // Stocke le chemin de l'image

    private String thumbnailName; // Chemin de la miniature

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
