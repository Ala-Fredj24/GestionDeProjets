# Build stage
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build production bundle
RUN npm run build

# Runtime stage - use nginx to serve the Angular app
FROM nginx:alpine

# Copy custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the built Angular browser bundle to nginx.
# Angular SSR builds emit index.csr.html, so rename it to index.html for nginx.
COPY --from=builder /app/dist/frontend/browser/ /usr/share/nginx/html/
RUN rm -f /usr/share/nginx/html/index.html && \
    if [ -f /usr/share/nginx/html/index.csr.html ]; then \
      mv /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html; \
    fi

# Run nginx as the non-root user already provided by the official image.
RUN chown -R nginx:nginx /usr/share/nginx/html /var/cache/nginx /var/log/nginx /var/run

USER nginx

# Expose port
EXPOSE 4200

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:4200/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
