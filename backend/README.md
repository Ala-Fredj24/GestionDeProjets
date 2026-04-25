# Backend - GestionDeProjets

API Spring Boot pour la gestion des projets, tâches, employés, sécurité JWT et rapports financiers.

## Prérequis

- Java 17
- Maven Wrapper inclus
- MySQL 8

## Lancement local

```bash
cd backend
bash mvnw spring-boot:run
```

Application disponible sur :

- API : http://localhost:8085/api
- Swagger : http://localhost:8085/api/swagger-ui.html

## Tests

```bash
bash mvnw test
```

Compilation rapide :

```bash
bash mvnw -DskipTests compile
```

## Observabilité

Endpoints exposés :

- santé : `http://localhost:8085/api/actuator/health`
- métriques Prometheus : `http://localhost:8085/api/actuator/prometheus`

Les logs backend sont standardisés avec un `requestId` pour faciliter le diagnostic.

## Sécurité

- authentification JWT
- rôles `ADMIN` et `CHEF_PROJET`
- Swagger disponible
- endpoints d'observabilité publics limités à la santé et aux métriques

## Configuration

Fichiers principaux :

- `src/main/resources/application.properties`
- `src/main/resources/application-docker.properties`
- `src/main/resources/logback-spring.xml`
