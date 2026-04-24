# Phases du projet — version adaptée à `GestionDeProjets`

Ce fichier reflète l'état réel du repo actuel :
- backend Spring Boot (port `8085`)
- frontend Angular (port `4200`)
- base MySQL + phpMyAdmin via `docker-compose.yml`
- branches Git de travail basées sur `feat/...` et `chore/...`, avec intégration vers `dev`

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

## Phase 8 — Conteneurisation applicative complète (backend + frontend + mysql) [TODO]

**Objectif :** passer d'un `docker-compose` DB-only à une stack complète.

**Travaux :**
- ajouter `Dockerfile` backend (Spring Boot)
- ajouter `Dockerfile` frontend (Angular build + serveur web)
- compléter le `docker-compose.yml` racine avec `backend`, `frontend`, `mysql`
- externaliser les variables d'environnement (sans secrets en dur)
- documenter la procédure unique de démarrage local conteneurisé

**Définition of done :**
- la stack complète démarre via Docker sans lancer manuellement backend/frontend

**Branch git proposée :** `feat/devops-conteneurisation-stack-complete`

---

## Phase 9 — CI GitHub Actions (build/test backend + frontend) [TODO]

**Objectif :** automatiser les vérifications à chaque push/PR.

**Travaux :**
- créer `.github/workflows/ci.yml`
- job backend : build + tests Maven
- job frontend : build + tests Angular
- cache Maven/npm
- déclenchement sur `push` et `pull_request` (branches `feat/**`, `chore/**`, `dev`)

**Définition of done :**
- toute régression de build/test bloque la PR vers `dev`

**Branch git proposée :** `feat/devops-ci-github-actions`

---

## Phase 10 — Quality gate & sécurité pipeline [TODO]

**Objectif :** renforcer la CI avec contrôle qualité et sécurité.

**Travaux :**
- analyse qualité (ex: SonarCloud)
- scan de dépendances Maven/npm
- scan SAST (ex: CodeQL)
- règles de blocage explicites sur vulnérabilités critiques

**Définition of done :**
- les checks qualité/sécurité requis sont obligatoires avant merge vers `dev`

**Branch git proposée :** `feat/devops-quality-security-gate`

---

## Phase 11 — Déploiement local orchestré (Kubernetes optionnel) [TODO]

**Objectif :** préparer un mode de déploiement démontrable pour la soutenance.

**Approche recommandée :**
1. finaliser d'abord Docker Compose complet (phase 8)
2. ensuite ajouter des manifests Kubernetes locaux si nécessaire (`k8s/`)

**Définition of done :**
- un guide reproductible permet de déployer la stack localement de bout en bout

**Branch git proposée :** `feat/devops-deploiement-local-orchestre`

---

## Phase 12 — Observabilité (logs + métriques) [TODO]

**Objectif :** rendre le diagnostic opérationnel (application + infra).

**Travaux :**
- standardiser les logs backend (niveaux, contexte, erreurs)
- exposer des métriques backend (Actuator / Prometheus)
- tableau de bord de base (Grafana) pour la démo

**Définition of done :**
- incidents courants identifiables rapidement via logs et dashboards

**Branch git proposée :** `feat/devops-observabilite-logs-metrics`

---

## Phase 13 — Finalisation et soutenance [TODO]

**Objectif :** figer une version stable et présentable.

**Travaux :**
- nettoyage documentation (README racine + backend + frontend)
- script/scénario de démonstration reproductible
- vérification finale cohérence code/config/docs

**Définition of done :**
- projet livrable, démontrable et aligné avec l'implémentation réelle

**Branch git proposée :** `chore/finalisation-livrable-soutenance`

---
