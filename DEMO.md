# Scénario de démonstration

Ce document propose un déroulé simple et reproductible pour la soutenance.

## 1. Préparer la stack

Option recommandée :

```bash
bash scripts/demo-start.sh
```

Option manuelle :

```bash
docker-compose up --build
```

## 2. Vérifier les URLs

- Frontend : http://localhost:4200
- Backend : http://localhost:8085/api
- Swagger : http://localhost:8085/api/swagger-ui.html
- phpMyAdmin : http://localhost:8084
- Prometheus : http://localhost:9090
- Grafana : http://localhost:3000

## 3. Déroulé conseillé

### Étape A - Présenter l'architecture

- frontend Angular
- backend Spring Boot
- MySQL + phpMyAdmin
- CI/CD GitHub Actions
- quality gate sécurité
- observabilité Prometheus/Grafana
- option Kubernetes locale

### Étape B - Montrer l'authentification

- ouvrir l'écran de login
- se connecter avec un utilisateur de démonstration
- montrer la navigation protégée

### Étape C - Montrer le métier

- créer ou lister des projets
- créer ou lister des tâches
- montrer l'affectation des ressources
- afficher le rapport financier

### Étape D - Montrer l'API

- ouvrir Swagger
- montrer les endpoints sécurisés
- montrer les rôles backend

### Étape E - Montrer l'observabilité

- ouvrir `http://localhost:8085/api/actuator/health`
- ouvrir `http://localhost:8085/api/actuator/prometheus`
- ouvrir Grafana et montrer le dashboard provisionné
- ouvrir Prometheus et vérifier le scrape backend

### Étape F - Montrer le DevOps

- ouvrir `phasesDevOps.md`
- rappeler les phases 8 à 13
- montrer les workflows GitHub Actions
- montrer `k8s/` pour le déploiement local orchestré

## 4. Points clés à dire

- projet exécutable localement de bout en bout
- pipeline qualité et sécurité en place
- déploiement conteneurisé disponible
- déploiement Kubernetes local préparé
- observabilité intégrée

## 5. Arrêt de la stack

```bash
docker-compose down
```
