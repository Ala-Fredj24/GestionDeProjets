# phpMyAdmin For Kubernetes MySQL

This phpMyAdmin container connects to the MySQL service running inside the
`gestiondeprojets` Kubernetes namespace through a local `kubectl port-forward`.

Start the MySQL tunnel first:

```bash
./admintools/phpmyadmin/start-mysql-tunnel.sh
```

Then start phpMyAdmin:

```bash
docker compose -f admintools/phpmyadmin/docker-compose.yml up -d
```

Open:

```text
http://localhost:8084
```

Login with the values from `k8s/overlays/local/.env`, for example:

```text
Server: host.docker.internal:3307
Username: appuser
Password: apppass
```

The important detail is that phpMyAdmin runs in Docker, so it cannot reach a
`kubectl port-forward` that only listens on `127.0.0.1`. The helper script binds
the tunnel to both `127.0.0.1` and Docker's host gateway address.
