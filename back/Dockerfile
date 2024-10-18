# Étape 1 : Construction de l'application Spring Boot
FROM maven:3.9.4-amazoncorretto-21 AS build
WORKDIR /workspace
COPY pom.xml /workspace
COPY src workspace/src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application Spring Boot
FROM openjdk:21-jdk-slim
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
