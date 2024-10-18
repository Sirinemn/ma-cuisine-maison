package fr.sirine.starter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentation de l'API MonStarter")
                        .version("1.0")
                        .description("Ce projet implémente une authentification à deux facteurs (2FA) sécurisée pour une application web utilisant Spring Boot pour le backend et Angular pour le frontend. Le système utilise l'e-mail comme méthode de vérification secondaire pour confirmer l'identité de l'utilisateur lors de la connexion."));
    }

}
