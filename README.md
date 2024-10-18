# My personnel project
Ce projet implémente une authentification à deux facteurs (2FA) sécurisée pour une application web utilisant Spring Boot pour le backend et Angular pour le frontend. Le système utilise l'e-mail comme méthode de vérification secondaire pour confirmer l'identité de l'utilisateur lors de la connexion.

## Fonctionnalités:

* Authentification de base avec nom d'utilisateur et mot de passe
* Enregistrement de l'utilisateur avec vérification par e-mail
* Activation de la 2FA par e-mail
* Génération et envoi d'un code de vérification unique par e-mail
* Validation du code de vérification lors de la connexion
* Désactivation de la 2FA si nécessaire

## Technologie utilisés:

* Spring Boot 3: Un framework puissant pour créer des applications basées sur Java.
* Spring Security 6: Fournit des mécanismes d’authentification et d’autorisation pour sécuriser l’application.
* JWT Token Authentication: Assure une communication sécurisée entre le client et le serveur.
* Spring Data JPA: Simplifie l'accès aux données et la persistance à l'aide de l'API Java Persistence.
* JSR-303 and Spring Validation: Permet la validation des objets en fonction des annotations.
* Swagger UI Documentation: Génère de la documentation pour les points de terminaison de l'API.
* Docker : facilite la conteneurisation de l'application backend pour le déploiement.

## Instructions de configuration de Backend:

1. Run the docker-compose file:
 docker-compose up -d
2. Ce positionner sur le dossier back:
  cd back
3. Install dependencies (assuming Maven is installed):
 mvn clean install
4. Exécutez l'application mais remplacez d'abord le x.x.x par la version actuelle du fichier pom.xml:
  java -jar target/book-network-api-x.x.x.jar
5. Accédez à la documentation de l'API à l'aide de l'interface utilisateur Swagger:
  http://localhost:8080/swagger-ui/index.html