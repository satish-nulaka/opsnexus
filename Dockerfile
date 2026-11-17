# Multi-stage production-grade Dockerfile for TaskFlow API
# Stage 1: Dependencies & Build
FROM node:20-alpine AS builder

WORKDIR /usr/src/app

COPY package*.json ./
RUN npm ci

COPY . .

# Run linting and tests during container validation build
RUN npm run lint
RUN npm run test:unit

# Stage 2: Minimal Production Image
FROM node:20-alpine AS production

LABEL maintainer="Deakin University SIT223/SIT753 DevOps Team"
LABEL description="TaskFlow API Production Container"
LABEL version="1.0.0"

ENV NODE_ENV=production
ENV PORT=3000

WORKDIR /usr/src/app

# Install dumb-init to properly forward signals to Node.js
RUN apk add --no-cache dumb-init curl

# Copy dependency definitions and install production-only dependencies
COPY package*.json ./
RUN npm ci --only=production && npm cache clean --force

# Copy application source code
COPY --chown=node:node src/ ./src/

# Security best practice: Run as unprivileged user
USER node

# Expose HTTP port
EXPOSE 3000

# Container healthcheck probe
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:3000/api/health || exit 1

ENTRYPOINT ["/usr/bin/dumb-init", "--"]
CMD ["node", "src/server.js"]
