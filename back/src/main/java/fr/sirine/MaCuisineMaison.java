package fr.sirine;

import fr.sirine.cuisine.category.Category;
import fr.sirine.cuisine.category.CategoryRepository;
import fr.sirine.cuisine.category.RecipeCategory;
import fr.sirine.starter.role.RoleRepository;
import fr.sirine.starter.role.Role;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.ArrayList;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class MaCuisineMaison {

	public static void main(String[] args) {

		SpringApplication.run(MaCuisineMaison.class, args);
	}
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
		return args -> {
			// Initialiser les rôles
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
			// Initialiser l'utilisateur admin
			if (userRepository.findByEmail("admin@mail.fr").isEmpty()) {
				User admin = new User();
				admin.setFirstname("admin");
				admin.setEmail("admin@mail.fr");
				admin.setLastname("Last");
				admin.setPseudo("admin");
				admin.setEnabled(true);
				admin.setAccountLocked(false);
				admin.setPassword(passwordEncoder().encode("adminpassword"));
				Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
				admin.setRoles(new ArrayList<>(Collections.singleton(adminRole)));
				userRepository.save(admin);
			}
			// Initialiser les catégories
			for (RecipeCategory recipeCategory : RecipeCategory.values()) {
				if (!categoryRepository.existsByName(recipeCategory)) {
					Category category = new Category();
					category.setName(recipeCategory);
					categoryRepository.save(category);
				}
			}
		};
	}

}
