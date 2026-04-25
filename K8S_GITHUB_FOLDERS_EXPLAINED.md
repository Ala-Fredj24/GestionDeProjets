# What The k8s And .github Folders Do

This file explains the two imported folders in plain language.

## The k8s Folder

The `k8s` folder describes how Kubernetes should run your application.

It does not install Kubernetes by itself. It gives `kubectl`, kind, and Argo CD the instructions they need.

### k8s/base

`k8s/base` is the shared application definition.

It contains:

- `namespace.yaml`: creates the `gestiondeprojets` namespace
- `mysql`: runs MySQL with a PVC for persistent data
- `backend`: runs the Spring Boot API on port `8085`
- `frontend`: runs the Angular build through nginx on port `4200`
- `assets/01-schema.sql`: bootstraps the `gestion_projet` database
- `monitoring`: optional Prometheus and Grafana manifests used by GitOps

The base is environment-neutral. It uses internal ClusterIP services and does not decide how secrets or public access are handled.

### k8s/overlays/local

`k8s/overlays/local` adapts the base for your laptop.

It does three important things:

- creates `gestiondeprojets-secrets` from `k8s/overlays/local/.env`
- changes images to the local `:local` tag
- exposes frontend and backend through kind NodePorts

Use it when you want to deploy manually:

```bash
kubectl apply -k k8s/overlays/local
```

### k8s/overlays/gitops

`k8s/overlays/gitops` is the source of truth for Argo CD.

It is similar to the local overlay, but with two key differences:

- it does not generate secrets from `.env`
- it points to GHCR image names and SHA tags

The CD workflow edits this file automatically when new images are published.

### k8s/argocd

`k8s/argocd` contains Argo CD bootstrap files.

It contains:

- `namespace.yaml`: creates the `argocd` namespace
- `gestiondeprojets-app.yaml`: tells Argo CD to watch this GitHub repo and apply `k8s/overlays/gitops`

Argo CD works by comparing Git with the cluster. If Git changes, Argo CD syncs the cluster.

## The .github Folder

The `.github/workflows` folder contains GitHub Actions automation.

It does not run on your laptop. It runs on GitHub after pushes, pull requests, schedules, or workflow events.

### ci.yml

`ci.yml` validates application quality.

It runs:

- backend Maven tests and build
- frontend npm install, Prettier check, unit tests with coverage, npm audit, and build
- optional SonarCloud analysis when `SONAR_TOKEN` is configured

The CD workflow waits for this workflow to pass before publishing images.

### security.yml

`security.yml` runs security checks.

It runs:

- CodeQL static analysis for Java and TypeScript
- OWASP Dependency-Check for Maven dependencies
- Trivy scans for backend and frontend Docker images

The CD workflow also waits for this workflow to pass.

### cd.yml

`cd.yml` publishes Docker images and updates GitOps.

It runs only on pushes to `main`.

Its logic is:

1. Wait for `CI` and `Security` to pass on the same commit.
2. Detect whether `backend/` or `frontend/` changed.
3. Build and push only the changed images to GHCR.
4. Create a GitOps PR that updates `k8s/overlays/gitops/kustomization.yaml`.
5. Enable auto-merge for that GitOps PR.

This keeps deployment changes visible in Git instead of secretly changing the cluster.

### pr-validation.yml

`pr-validation.yml` is a light pull request check for normal contributor PRs.

It validates YAML in:

- `.github/workflows`
- `k8s`
- `docker-compose.yml`

### gitops-validation.yml

`gitops-validation.yml` is a lighter check for bot-created GitOps PRs.

It validates:

- YAML syntax
- `kustomize build k8s/overlays/gitops`
- rendered Kubernetes manifests with offline schema validation

This avoids rebuilding the whole app just because the bot changed image tags.

## What This Setup Gives You

You get:

- local Kubernetes learning with kind
- clean separation between base manifests and environment overlays
- generated local secrets that stay out of Git
- Argo CD GitOps deployment from the `main` branch
- Docker image publishing to GHCR
- automatic image tag bumps through pull requests
- CI checks before images are published
- security checks before images are published
- optional monitoring with Prometheus and Grafana

## What You Still Do Manually

You still manually:

- install Docker, kubectl, kind, and Argo CD
- create local `.env` files
- create the first Kubernetes secret for Argo CD
- configure GitHub repository settings and branch protection
- configure optional SonarCloud and NVD secrets
- decide whether GHCR images should be public or private

For a first learning path, public GHCR packages are simpler. Private GHCR packages are more production-like, but require an image pull secret in Kubernetes.
