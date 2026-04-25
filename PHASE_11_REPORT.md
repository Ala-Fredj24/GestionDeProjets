# Phase 11: Déploiement local orchestré — Guide complet

## Objectif

Fournir un mode de déploiement local reproductible, démontrable et aligné avec l'architecture du projet pour la soutenance.

## Livrables

- manifests Kubernetes dans `k8s/`
- séparation de la configuration via `ConfigMap` et `Secret`
- persistance MySQL via `PersistentVolumeClaim`
- déploiement des services `mysql`, `backend`, `frontend`, `phpmyadmin`
- guide d'exécution local dans `k8s/README.md`

## Fichiers ajoutés

- `k8s/namespace.yaml`
- `k8s/configmap.yaml`
- `k8s/secret.yaml`
- `k8s/mysql.yaml`
- `k8s/backend.yaml`
- `k8s/frontend.yaml`
- `k8s/phpmyadmin.yaml`
- `k8s/README.md`

## Scénario de déploiement retenu

Le dépôt conserve Docker Compose comme chemin principal de run local, et ajoute un scénario Kubernetes local pour une démo orchestrée.

Le workflow retenu est :

1. démarrer Minikube
2. builder les images applicatives localement
3. appliquer les manifests Kubernetes
4. exposer `backend`, `frontend` et `phpmyadmin` via `kubectl port-forward`

## Services déployés

- `mysql` : base persistante du projet
- `backend` : application Spring Boot sur `8085`
- `frontend` : application Angular servie sur `4200`
- `phpmyadmin` : interface d'administration MySQL

## Définition of done couverte

- déploiement local documenté de bout en bout
- stack complète déployable sur cluster local
- procédure de démonstration reproductible
- artefacts de déploiement versionnés dans le repo

## Commandes clés

```bash
minikube start
eval "$(minikube -p minikube docker-env)"
docker build -t gestionprojets-backend:local ./backend
docker build -t gestionprojets-frontend:local ./frontend
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/mysql.yaml
kubectl apply -f k8s/backend.yaml
kubectl apply -f k8s/frontend.yaml
kubectl apply -f k8s/phpmyadmin.yaml
kubectl get all -n gestionprojets
kubectl port-forward svc/backend 8085:8085 -n gestionprojets
kubectl port-forward svc/frontend 4200:4200 -n gestionprojets
kubectl port-forward svc/phpmyadmin 8084:8084 -n gestionprojets
```
