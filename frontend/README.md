# Frontend - GestionDeProjets

Interface Angular de gestion des projets, tâches, employés, rapports financiers et authentification.

## Prérequis

- Node.js 20+
- npm

## Lancement local

```bash
cd frontend
npm install
npm start
```

Application disponible sur :

- Frontend : http://localhost:4200

Le frontend consomme l'API backend sur :

- `http://localhost:8085/api`

## Build

```bash
npm run build
```

## Tests

```bash
npm test
```

Couverture :

```bash
npm run test:coverage
```

## Fonctionnalités principales

- login avec JWT
- routage selon rôle
- CRUD projets
- CRUD tâches
- affectation des ressources
- rapports financiers

## Remarque démo

Pour une soutenance locale complète, le plus simple est d'utiliser la stack Docker à la racine du projet.
