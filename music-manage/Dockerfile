# Multi-stage: build SPA → nginx static
FROM node:20-alpine AS builder
WORKDIR /app

ARG NODE_HOST=http://localhost:8888
ENV NODE_HOST=$NODE_HOST

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
