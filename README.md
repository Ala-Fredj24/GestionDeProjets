# GestionDeProjets

Application de gestion de projets avec :

- backend Spring Boot
- frontend Angular
- base MySQL
- conteneurisation Docker Compose
- pipeline CI/CD GitHub Actions
- quality gate et sécurité
- déploiement Kubernetes local
- observabilité Prometheus + Grafana

## Stack technique

- Java 17
- Spring Boot 4
- Angular 21
- MySQL 8
- Docker Compose
- GitHub Actions
- SonarCloud, CodeQL, OWASP Dependency-Check
- Kubernetes local
- Prometheus + Grafana

## Structure du projet

```text
.
├── backend/                 # API Spring Boot
├── frontend/                # Application Angular
├── .github/workflows/       # CI, quality gate, security
├── k8s/                     # Déploiement Kubernetes local
├── observability/           # Prometheus + Grafana
├── DOCKER.md                # Guide Docker
├── TESTING.md               # Stratégie de tests
├── DEMO.md                  # Scénario de soutenance
└── phasesDevOps.md          # Suivi des phases
```

## Démarrage rapide

### Option 1 : stack Docker complète

```bash
docker-compose up --build
```

Services disponibles :

- Frontend : http://localhost:4200
- Backend API : http://localhost:8085/api
- Swagger : http://localhost:8085/api/swagger-ui.html
- phpMyAdmin : http://localhost:8084
- Prometheus : http://localhost:9090
- Grafana : http://localhost:3000

### Option 2 : script de démo

```bash
bash scripts/demo-start.sh
```

## Documentation

- [DOCKER.md](DOCKER.md) : exécution complète avec Docker
- [TESTING.md](TESTING.md) : stratégie de tests et couverture
- [PHASE_10_REPORT.md](PHASE_10_REPORT.md) : quality gate et sécurité
- [PHASE_11_REPORT.md](PHASE_11_REPORT.md) : déploiement Kubernetes local
- [PHASE_12_REPORT.md](PHASE_12_REPORT.md) : observabilité
- [PHASE_13_REPORT.md](PHASE_13_REPORT.md) : finalisation et soutenance
- [DEMO.md](DEMO.md) : déroulé reproductible de démonstration

## Commandes utiles

### Backend

```bash
cd backend
bash mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm start
```

### Tests backend

```bash
cd backend
bash mvnw test
```

### Tests frontend

```bash
cd frontend
npm test
```

## Déploiement local Kubernetes

Voir [k8s/README.md](k8s/README.md).

## Observabilité

- métriques : `/api/actuator/prometheus`
- santé : `/api/actuator/health`
- dashboard Grafana provisionné automatiquement

## Statut projet

Le projet est finalisé pour une démonstration locale complète et documentée.
