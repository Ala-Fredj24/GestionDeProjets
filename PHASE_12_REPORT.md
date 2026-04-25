# Phase 12: Observabilité — Guide complet

## Objectif

Rendre le diagnostic applicatif et infrastructure plus rapide grâce à des logs standardisés et à une stack de métriques locale.

## Livrables

- logs backend enrichis avec `requestId`
- exposition des métriques Spring Boot via Actuator + Prometheus
- stack locale `prometheus` + `grafana` dans `docker-compose.yml`
- provisioning automatique Grafana
- dashboard Grafana de base pour la démonstration

## Changements réalisés

- ajout de `spring-boot-starter-actuator`
- ajout de `micrometer-registry-prometheus`
- ajout du filtre `RequestCorrelationFilter`
- ajout de `logback-spring.xml`
- exposition de `health`, `info` et `prometheus`
- autorisation des endpoints de monitoring dans `SecurityConfig`
- ajout des services `prometheus` et `grafana` dans `docker-compose.yml`
- ajout de la configuration Prometheus et du dashboard Grafana

## Endpoints utiles

- `http://localhost:8085/api/actuator/health`
- `http://localhost:8085/api/actuator/prometheus`
- `http://localhost:9090`
- `http://localhost:3000`

## Dashboard Grafana inclus

Le dashboard fourni affiche :

- disponibilité backend
- débit HTTP
- latence P95 par URI
- usage CPU système
- mémoire JVM
- répartition des réponses HTTP par statut

## Démarrage local

```bash
docker-compose up --build
```

Puis ouvrir :

- Frontend : `http://localhost:4200`
- Backend : `http://localhost:8085/api`
- Prometheus : `http://localhost:9090`
- Grafana : `http://localhost:3000`

Identifiants Grafana par défaut :

- utilisateur : `admin`
- mot de passe : `admin`

## Définition of done couverte

- logs backend standardisés pour faciliter le diagnostic
- métriques backend exportées pour Prometheus
- tableau de bord Grafana prêt pour la démo
- observabilité utilisable localement sur la stack Docker
