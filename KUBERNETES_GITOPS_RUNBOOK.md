# Kubernetes And GitOps Runbook

This is the manual checklist to run GestionDeProjets locally on Kubernetes with kind, then connect the same kind cluster to Argo CD for GitOps.

The project now uses:

- namespace: `gestiondeprojets`
- database: `gestion_projet`
- local images: `gestiondeprojets-backend:local` and `gestiondeprojets-frontend:local`
- GitOps images: `ghcr.io/ala-fredj24/gestiondeprojets-backend:<tag>` and `ghcr.io/ala-fredj24/gestiondeprojets-frontend:<tag>`
- frontend URL through kind: `http://localhost:30080`
- backend direct URL through kind: `http://localhost:30085`

## 1. Install Local Tools

Install these on your laptop:

- Docker
- kubectl
- kind
- Git
- GitHub CLI, optional but useful: `gh`

Check them:

```bash
docker --version
kubectl version --client
kind version
git --version
```

## 2. Prepare Local Kubernetes Secrets

From the repository root:

```bash
cp k8s/overlays/local/.env.example k8s/overlays/local/.env
```

Open `k8s/overlays/local/.env` and replace the passwords. Generate a real JWT secret:

```bash
openssl rand -base64 32
```

Put that value in:

```env
JWT_SECRET=your_generated_value_here
```

Do not commit `k8s/overlays/local/.env`.

## 3. Create The kind Cluster

```bash
kind create cluster --config k8s/overlays/local/kind-cluster.yaml
kubectl cluster-info --context kind-gestiondeprojets
```

The kind config maps these ports from the cluster to your laptop:

- `30080` for the frontend
- `30085` for direct backend access

## 4. Build And Load Local Images

```bash
docker build -t gestiondeprojets-backend:local ./backend
docker build -t gestiondeprojets-frontend:local ./frontend

kind load docker-image gestiondeprojets-backend:local --name gestiondeprojets
kind load docker-image gestiondeprojets-frontend:local --name gestiondeprojets
```

## 5. Deploy Directly With kubectl

```bash
kubectl apply -k k8s/overlays/local
kubectl get pods -n gestiondeprojets
kubectl get svc -n gestiondeprojets
```

Wait until the pods are ready:

```bash
kubectl rollout status deployment/mysql -n gestiondeprojets
kubectl rollout status deployment/backend -n gestiondeprojets
kubectl rollout status deployment/frontend -n gestiondeprojets
```

Open:

- frontend: `http://localhost:30080`
- backend health: `http://localhost:30085/actuator/health`
- Swagger: `http://localhost:30085/swagger-ui.html`

If the frontend still shows the default nginx welcome page, rebuild the
frontend image and load it again into kind. The Dockerfile now renames the
Angular SSR output (`index.csr.html`) to `index.html` so nginx serves the app
instead of its placeholder page.

```bash
docker build -t gestiondeprojets-frontend:local ./frontend
kind load docker-image gestiondeprojets-frontend:local --name gestiondeprojets
kubectl rollout restart deployment/frontend -n gestiondeprojets
kubectl rollout status deployment/frontend -n gestiondeprojets
```

Useful debug commands:

```bash
kubectl logs deployment/backend -n gestiondeprojets
kubectl logs deployment/frontend -n gestiondeprojets
kubectl describe pod -l component=backend -n gestiondeprojets
```

## 6. Reset Local Kubernetes

Delete only the app:

```bash
kubectl delete -k k8s/overlays/local
```

Delete the whole kind cluster:

```bash
kind delete cluster --name gestiondeprojets
```

## 7. Prepare GitHub For CI/CD

In GitHub, go to your repository settings.

Actions permissions:

- Settings -> Actions -> General
- Workflow permissions: choose `Read and write permissions`
- Enable `Allow GitHub Actions to create and approve pull requests`

Repository secrets:

- `APP_JWT_SECRET`: recommended. Use `openssl rand -base64 32`
- `SONAR_TOKEN`: optional. If missing, the SonarCloud step is skipped
- `NVD_API_KEY`: optional but recommended for OWASP Dependency-Check stability

Repository settings:

- Enable auto-merge if you want the GitOps PR to merge automatically after checks pass
- For branch protection, start simple while learning: require PRs into `main`, then add required checks after you see the exact check names in your first PR

GHCR package visibility:

- For the easiest local learning path, make the generated GHCR packages public
- If you keep GHCR packages private, you must add a Kubernetes `imagePullSecret` later

## 8. Understand The GitOps Flow

The automated flow is:

1. You merge code to `main`.
2. `CI` and `Security` run.
3. `CD` waits for both to pass on the same commit.
4. `CD` builds only changed app images.
5. `CD` pushes images to GHCR with a tag like `sha-a1b2c3d`.
6. `CD` creates a PR that edits `k8s/overlays/gitops/kustomization.yaml`.
7. After that GitOps PR merges, Argo CD sees the new Git state.
8. Argo CD syncs the kind cluster to match Git.

Important first-time note: the first GitOps deployment needs real GHCR image tags. The current placeholder tags in `k8s/overlays/gitops/kustomization.yaml` are replaced by the first successful CD image bump.

## 9. Install Argo CD In The kind Cluster

Create the cluster first if you deleted it:

```bash
kind create cluster --config k8s/overlays/local/kind-cluster.yaml
```

Install Argo CD:

```bash
kubectl apply -f k8s/argocd/namespace.yaml
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl wait --for=condition=available deployment/argocd-server -n argocd --timeout=300s
```

Get the initial admin password:

```bash
kubectl -n argocd get secret argocd-initial-admin-secret \
  -o jsonpath="{.data.password}" | base64 -d
echo
```

Open the Argo CD UI:

```bash
kubectl port-forward svc/argocd-server -n argocd 8080:443
```

Then open `https://localhost:8080`.

Login:

- username: `admin`
- password: the value from the command above

## 10. Bootstrap The App Secret For Argo CD

Argo CD does not read your ignored `.env` file from Git. Create the secret manually once:

```bash
kubectl create namespace gestiondeprojets --dry-run=client -o yaml | kubectl apply -f -

kubectl create secret generic gestiondeprojets-secrets \
  --from-env-file=k8s/overlays/local/.env \
  -n gestiondeprojets \
  --dry-run=client -o yaml | kubectl apply -f -
```

## 11. Create The Argo CD Application

```bash
kubectl apply -f k8s/argocd/gestiondeprojets-app.yaml
kubectl get applications -n argocd
```

In the Argo CD UI, open `gestiondeprojets`.

If the GitOps image tags have already been bumped by CD, Argo CD should sync the app. If it shows image pull errors, check GHCR visibility or configure an image pull secret.

## 12. Useful GitOps Checks

```bash
kubectl get pods -n gestiondeprojets
kubectl get svc -n gestiondeprojets
kubectl logs deployment/backend -n gestiondeprojets
kubectl describe application gestiondeprojets -n argocd
```

Access the app after Argo CD syncs it:

- frontend: `http://localhost:30080`
- backend health: `http://localhost:30085/actuator/health`
- Swagger: `http://localhost:30085/swagger-ui.html`

Monitoring in the GitOps overlay:

```bash
kubectl port-forward svc/gestiondeprojets-grafana 3000:3000 -n gestiondeprojets
kubectl port-forward svc/gestiondeprojets-prometheus 9090:9090 -n gestiondeprojets
```

Then open:

- Grafana: `http://localhost:3000`
- Prometheus: `http://localhost:9090`

Grafana login:

- username: `admin`
- password: `admin`
