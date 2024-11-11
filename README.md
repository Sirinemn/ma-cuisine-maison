# Ma Cuisine Maison

**Ma Cuisine Maison** est une application web permettant aux utilisateurs de gérer des recettes culinaires, de commenter celles des autres, et de rechercher des recettes par catégorie. L'application permet également aux utilisateurs d'ajouter des images pour illustrer leurs recettes, offrant une expérience enrichissante et visuelle.

## Table des matières

- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Démarrage](#démarrage)
- [Architecture](#architecture)
- [Utilisation](#utilisation)
- [Contributions](#contributions)
- [Licence](#licence)

## Fonctionnalités

- **Gestion des utilisateurs** : Inscription, connexion et gestion des rôles.
- **Ajout et gestion des recettes** : Création, modification et suppression de recettes par les utilisateurs.
- **Recherche par catégorie** : Les utilisateurs peuvent rechercher des recettes selon des catégories prédéfinies (ex. : Entrées, Plats principaux, Desserts, etc.).
- **Commentaires** : Ajout de commentaires sur les recettes.
- **Gestion des images** : Ajout d'images lors de la création des recettes avec redimensionnement pour différents affichages.
- **Suggestions d'ingrédients** : Utilisation de l'API externe Spoonacular pour suggérer des ingrédients avec autocomplétion.

## Technologies utilisées

- **Front-end** : Angular, TypeScript
- **Back-end** : Spring Boot, Java 21
- **Base de données** : MySQL
- **API externe** : Spoonacular (pour les suggestions d'ingrédients)
- **Sécurité** : Spring Security, JWT
- **Conteneurisation** : Docker, Docker Compose
- **Tests** : Cypress pour le front-end, JUnit pour le back-end
- **Swagger UI Documentation**: Génère de la documentation pour les points de terminaison de l'API.

## Prérequis

Assurez-vous d'avoir installé les logiciels suivants sur votre machine :

- [Node.js](https://nodejs.org/) (pour Angular)
- [Java 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)
- [MySQL](https://www.mysql.com/)

## Installation

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/Sirinemn/ma-cuisine-maison.git
   cd ma-cuisine-maison
2. **Configurer le back-end :**
    Accédez au répertoire backend et modifiez les configurations de connexion à la base de donées dans application.properties.
3. **Installer les dépendances:**
 - **Front-end :**
    ```bash
    cd front
    npm install
 - **Back-end :**
    ```bash
    cd back
    mvn clean install

## Démarrage
    Pour lancer l'application, suivez ces étapes:
1. **Démarrer les services conteneurisés:**
    ```bash
    docker-compose up -d
2. **Démarrer le front-end :**
    ```bash
    cd front
    ng serve
   L'application sera disponible à l'adresse : http://localhost:4200.
3. **Démarrer le back-end :**
    ```bash
    cd back
    mvn spring-boot:run

## Architrecture
L'application suit une architecture client-serveur :

- Front-end : Construit avec Angular pour gérer l'interface utilisateur et la logique côté client.
- Back-end : Développé avec Spring Boot, fournissant des services RESTful pour la gestion des recettes, des utilisateurs, des images et des commentaires.
- Base de données : MySQL pour le stockage des données structurées.
- Service de messagerie : MailDev est utilisé pour tester l'envoi des emails, en particulier pour l'inscription à deux facteurs.
- API externe : Utilisée pour les suggestions d'ingrédients (Spoonacular).

## Utilisation
- Inscription/Connexion : Créez un compte ou connectez-vous pour accéder aux fonctionnalités complètes. L'inscription est sécurisée avec une vérification à deux facteurs par email.
- Ajouter une recette : Allez dans la section « Ajouter une recette », remplissez le formulaire et ajoutez une image.
- Consulter des recettes : Naviguez dans les catégories pour voir des recettes et leurs détails.
- Ajouter un commentaire : Partagez votre avis sur une recette.

## Contribution
Les contributions sont les bienvenues ! Pour contribuer :

1. Fork ce dépôt.
2. Créez une branche pour votre fonctionnalité ou correction (git checkout -b ma-fonctionnalité).
3. Commit vos modifications (git commit -m "Ajout d'une nouvelle fonctionnalité").
4. Push la branche (git push origin ma-fonctionnalité).
5. Créez une Pull Request.

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENCE pour plus de détails.

