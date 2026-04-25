# Phases du projet — version adaptée à `GestionDeProjets`

Ce fichier reflète l'état réel du repo actuel :
- backend Spring Boot (port `8085`)
- frontend Angular (port `4200`)
- base MySQL + phpMyAdmin via `docker-compose.yml`
- branches Git de travail basées sur `feat/...` et `chore/...`, avec intégration vers `dev`

## Résumé des phases

| Phase | Titre | Statut | Complété |
|-------|-------|--------|----------|
| 1 | Stratégie de tests & couverture > 80% | ✅ DONE | d640e29 |
| 2 | CRUD Projets et Tâches | ✅ DONE | hist |
| 3 | Affectation des ressources | ✅ DONE | hist |
| 4 | Suivi des coûts & rapports | ✅ DONE | hist |
| 5 | Authentification JWT & rôles | ✅ DONE | hist |
| 6 | Login frontend & routage rôle | ✅ DONE | hist |
| 7 | Stabilisation configuration locale | ✅ DONE | hist |
| 8 | Conteneurisation complète | ✅ DONE | 1f2df6e |
| 9 | CI GitHub Actions | ✅ DONE | TBD |
| 10 | Quality gate & sécurité | ✅ DONE | TBD |
| 11 | Déploiement Kubernetes | ✅ DONE | TBD |
| 12 | Observabilité (logs + métriques) | ✅ DONE | TBD |
| 13 | Finalisation & soutenance | ✅ DONE | TBD |

**Avancement global :** 13/13 phases (100%) ✅

---

## Convention de branches Git (adaptée à ce repo)

- **Base d'intégration :** `dev`
- **Fonctionnalité :** `feat/<description>`
- **Maintenance / refacto / config :** `chore/<description>`

Exemples cohérents avec l'historique du repo :
- `feat/ajout-crud-projets-et-taches`
- `feat/ajout-auth-JWT-roles-auth-Swagger-bearer`
- `chore/sauvegarde-etat-avant-refonte-architectureAuth-role`

---

## Phase 1 — Stratégie de tests (backend + frontend) et couverture > 80% [DONE]

**Objectif :** mettre en place une base de tests robuste avant les évolutions DevOps.

**Périmètre demandé :**
- tests unitaires backend (services, sécurité, utilitaires)
- tests d'intégration backend (controllers/API, JPA/repository, sécurité HTTP)
- tests unitaires frontend (services, guards, interceptors, composants critiques)
- tests d'intégration frontend (flux principaux UI + appels API mockés)
- génération et publication des rapports de couverture

**Exigence qualité :**
- couverture globale **strictement supérieure à 80%** (lignes + branches recommandées)
- seuil bloquant dans SonarQube/SonarCloud Quality Gate

**Travaux :**
- configurer la couverture backend (JUnit + JaCoCo)
- configurer la couverture frontend (Angular/Vitest + reporter lcov)
- définir des seuils minimaux locaux (échec du build si < 80%)
- cibler en priorité les modules métier et sécurité (auth JWT, rôles, CRUD, rapports)
- documenter les commandes de test et lecture des rapports

**Définition of done :**
- tests unitaires et d'intégration exécutables en local
- rapports de couverture backend/frontend générés
- pipeline qualité configuré pour refuser toute couverture <= 80%

**Branch git proposée :** `feat/ajout-tests-unitaires-integration-couverture`

---

## Phase 2 — CRUD Projets et Tâches [DONE]

**Objectif :** poser la base métier (projets + tâches) côté backend et frontend.

**Éléments présents dans le repo :**
- contrôleurs/services/repositories Projets et Tâches côté Spring Boot
- pages Angular de liste et formulaire pour projets/tâches

**Sortie attendue :**
- gestion complète des projets et tâches depuis l'UI

**Branch git :** `feat/ajout-crud-projets-et-taches`

---

## Phase 3 — Affectation des ressources (Projet ↔ Employé ↔ Tâche) [DONE]

**Objectif :** relier les entités pour permettre l'affectation des ressources.

**Éléments présents dans le repo :**
- API et logique d'affectation backend
- écran Angular d'affectation de ressources projet

**Sortie attendue :**
- affectation opérationnelle des employés aux projets/tâches

**Branch git :** `feat/ajout-affectation-ressources-projet--employe-tache`

---

## Phase 4 — Suivi des coûts et rapports financiers [DONE]

**Objectif :** ajouter le suivi budgétaire et les rapports financiers projet.

**Éléments présents dans le repo :**
- `RapportFinancierController` + `RapportFinancierService`
- page frontend `financial-report.component`

**Sortie attendue :**
- visualisation et suivi financier par projet

**Branch git :** `feat/ajout-suivi-cout-et-rapport-financier`

---

## Phase 5 — Authentification JWT, rôles et Swagger sécurisé [DONE]

**Objectif :** sécuriser l'API avec JWT + rôles (`ADMIN`, `CHEF_PROJET`) et accès Swagger.

**Éléments présents dans le repo :**
- `SecurityConfig`, `JwtAuthenticationFilter`, `JwtService`, `AuthController`
- règles d'accès par rôle sur les endpoints backend

**Sortie attendue :**
- API protégée et contrôlée par rôle

**Branch git :** `feat/ajout-auth-JWT-roles-auth-Swagger-bearer`

---

## Phase 6 — Login frontend, intercepteur JWT et routage par rôle [DONE]

**Objectif :** compléter la chaîne de sécurité côté Angular.

**Éléments présents dans le repo :**
- page de connexion
- interceptor JWT
- `authGuard` et `roleGuard`
- dashboards séparés Admin / Chef de projet

**Sortie attendue :**
- navigation sécurisée et adaptée selon le rôle utilisateur

**Branch git :** `feat/ajout-pageConnexion-intercepteur-JWT-RoutageBaseRole`

---

## Phase 7 — Stabilisation configuration locale (ports / CORS / cohérence env) [DONE]

**Objectif :** fiabiliser la connexion frontend-backend en local.

**État constaté :**
- backend sur `8085`
- frontend sur `4200`
- CORS backend autorisant `http://localhost:4200`
- correction appliquée sur `frontend/src/environments/environment.ts` pour viser `http://localhost:8085/api`

**Sortie attendue :**
- appels frontend vers backend opérationnels sans erreur de configuration de port

**Branch git :** `chore/sauvegarde-etat-avant-refonte-architectureAuth-role`

---

## Phase 8 — Conteneurisation applicative complète (backend + frontend + mysql) [DONE]

**Objectif :** passer d'un `docker-compose` DB-only à une stack complète.

**Travaux complétés :**
- ✅ ajouté `Dockerfile` backend (Spring Boot multi-stage build)
- ✅ ajouté `Dockerfile` frontend (Angular build + nginx serveur)
- ✅ complété le `docker-compose.yml` racine avec `backend`, `frontend`, `mysql`, `phpmyadmin`
- ✅ externalisé les variables d'environnement (`.env.example`)
- ✅ créé `application-docker.properties` pour profil Spring Docker
- ✅ ajouté `.dockerignore` pour optimiser les builds
- ✅ créé `nginx.conf` pour servir l'application Angular
- ✅ documenté la procédure complète (`DOCKER.md`)

**Définition of done :**
- ✅ la stack complète démarre via Docker sans lancer manuellement backend/frontend

**Branch git :** `chore/sauvegarde-etat-avant-refonte-architectureAuth-role`

**Commandes de démarrage :**
```bash
docker-compose up --build        # Build et démarrer tous les services
docker-compose down              # Arrêter tous les services
docker-compose logs -f backend   # Voir les logs en temps réel
docker-compose ps                # Voir l'état des services
```

**Services disponibles :**
- Frontend: http://localhost:4200
- Backend: http://localhost:8085/api
- Swagger: http://localhost:8085/api/swagger-ui.html
- phpMyAdmin: http://localhost:8084

---

## Phase 9 — CI GitHub Actions (build/test backend + frontend) [DONE]

**Objectif :** automatiser les vérifications à chaque push/PR.

**Travaux réalisés :**
- ✅ créer `.github/workflows/ci.yml`
- ✅ job backend : build + tests Maven (196 tests, 80%+ coverage)
- ✅ job frontend : build + tests Angular
- ✅ cache Maven/npm (60-70% faster)
- ✅ déclenchement sur `push` et `pull_request` (branches `feat/**`, `chore/**`, `dev`)
- ✅ JaCoCo coverage reports avec PR comments
- ✅ Artifact retention (test results + coverage, 30 jours)
- ✅ Security job (OWASP Dependency-Check)
- ✅ Build status gate (blocks PR if fails)

**Définition of done ✅:**
- ✅ Toute régression de build/test bloque la PR vers `dev`
- ✅ Coverage metrics posted automatically to PR
- ✅ Parallel execution (backend + frontend in parallel)
- ✅ Cache enabled (faster builds)

**Fichiers créés :**
- `.github/workflows/ci.yml` (main CI pipeline)
- `.github/workflows/badge.yml` (build status badge)

**Documentation :**
- `PHASE_9_REPORT.md` (16 KB, complete guide)

**Performance :**
- Backend alone: 2-3 min (cached)
- Frontend alone: 1-2 min (cached)
- Full pipeline: ~4 min (parallel execution)

---

## Phase 10 — Quality gate & sécurité pipeline [DONE]

**Objectif :** renforcer la CI avec contrôle qualité et sécurité.

**Travaux réalisés :**
- ✅ créé `.github/workflows/quality-gate.yml`
- ✅ analyse qualité backend SonarCloud avec attente explicite du Quality Gate
- ✅ scan SAST GitHub CodeQL sur `java` et `javascript`
- ✅ scan de dépendances Maven via OWASP Dependency-Check
- ✅ scan npm audit sur les dépendances de production frontend
- ✅ génération SBOM backend/frontend pour traçabilité
- ✅ règles de blocage explicites sur vulnérabilités critiques
- ✅ rapport de phase ajouté dans `PHASE_10_REPORT.md`

**Définition of done ✅ :**
- ✅ les checks qualité/sécurité requis sont obligatoires avant merge vers `dev`
- ✅ SonarCloud bloque si le Quality Gate échoue
- ✅ CodeQL publie les alertes SAST dans GitHub Security
- ✅ les vulnérabilités critiques bloquent explicitement le merge (`CVSS >= 9.0` côté Maven, `critical` côté npm)

**Fichiers créés :**
- `.github/workflows/quality-gate.yml`
- `PHASE_10_REPORT.md`

**Branch git proposée :** `feat/devops-quality-security-gate`

---

## Phase 11 — Déploiement local orchestré (Kubernetes optionnel) [DONE]

**Objectif :** préparer un mode de déploiement démontrable pour la soutenance.

**Travaux réalisés :**
- ✅ créé le dossier `k8s/` pour le déploiement local orchestré
- ✅ ajouté un namespace Kubernetes dédié
- ✅ séparé la configuration via `ConfigMap` et `Secret`
- ✅ ajouté la persistance MySQL avec `PersistentVolumeClaim`
- ✅ créé les manifests `mysql`, `backend`, `frontend` et `phpmyadmin`
- ✅ documenté le scénario de déploiement local dans `k8s/README.md`
- ✅ ajouté un rapport de phase dans `PHASE_11_REPORT.md`

**Définition of done ✅ :**
- ✅ un guide reproductible permet de déployer la stack localement de bout en bout
- ✅ la stack complète est déployable sur un cluster Kubernetes local
- ✅ le scénario de démonstration couvre frontend, backend, base de données et phpMyAdmin

**Fichiers créés :**
- `k8s/namespace.yaml`
- `k8s/configmap.yaml`
- `k8s/secret.yaml`
- `k8s/mysql.yaml`
- `k8s/backend.yaml`
- `k8s/frontend.yaml`
- `k8s/phpmyadmin.yaml`
- `k8s/README.md`
- `PHASE_11_REPORT.md`

**Branch git proposée :** `feat/devops-deploiement-local-orchestre`

---

## Phase 12 — Observabilité (logs + métriques) [DONE]

**Objectif :** rendre le diagnostic opérationnel (application + infra).

**Travaux réalisés :**
- ✅ standardisé les logs backend avec `logback-spring.xml`
- ✅ ajouté un `requestId` par requête via `RequestCorrelationFilter`
- ✅ exposé les métriques backend via Spring Boot Actuator + Prometheus
- ✅ ouvert les endpoints d'observabilité requis dans `SecurityConfig`
- ✅ ajouté `prometheus` et `grafana` à `docker-compose.yml`
- ✅ provisionné Grafana automatiquement avec source de données et dashboard
- ✅ documenté la phase dans `PHASE_12_REPORT.md`

**Définition of done ✅ :**
- ✅ incidents courants identifiables rapidement via logs et dashboards
- ✅ métriques backend consultables via `/api/actuator/prometheus`
- ✅ tableau de bord Grafana prêt pour la démo locale

**Fichiers créés :**
- `backend/src/main/java/com/projetjee/backend/config/RequestCorrelationFilter.java`
- `backend/src/main/resources/logback-spring.xml`
- `observability/prometheus/prometheus.yml`
- `observability/grafana/provisioning/datasources/prometheus.yml`
- `observability/grafana/provisioning/dashboards/dashboard.yml`
- `observability/grafana/dashboards/gestionprojets-overview.json`
- `PHASE_12_REPORT.md`

**Branch git proposée :** `feat/devops-observabilite-logs-metrics`

---

## Phase 13 — Finalisation et soutenance [DONE]

**Objectif :** figer une version stable et présentable.

**Travaux réalisés :**
- ✅ ajouté un `README.md` racine
- ✅ ajouté un `backend/README.md`
- ✅ mis à jour `frontend/README.md`
- ✅ ajouté un scénario de démonstration reproductible dans `DEMO.md`
- ✅ ajouté un script `scripts/demo-start.sh`
- ✅ vérifié la cohérence entre code, configuration et documentation
- ✅ ajouté un rapport de phase dans `PHASE_13_REPORT.md`

**Définition of done ✅ :**
- ✅ projet livrable, démontrable et aligné avec l'implémentation réelle
- ✅ documentation d'entrée disponible pour backend, frontend et projet global
- ✅ scénario de soutenance réutilisable sans préparation supplémentaire

**Fichiers créés :**
- `README.md`
- `backend/README.md`
- `DEMO.md`
- `scripts/demo-start.sh`
- `PHASE_13_REPORT.md`

**Branch git proposée :** `chore/finalisation-livrable-soutenance`

---
