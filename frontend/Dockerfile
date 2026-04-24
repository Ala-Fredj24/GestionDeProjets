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

# Copy built Angular app to nginx
COPY --from=builder /app/dist/gestion-projet /usr/share/nginx/html

# Create non-root user
RUN addgroup -g 1000 nginx && \
    adduser -u 1000 -S nginx -G nginx && \
    chown -R nginx:nginx /usr/share/nginx/html

USER nginx

# Expose port
EXPOSE 4200

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:4200/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
