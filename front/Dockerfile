# Étape 1 : Construction de l'application Angular
FROM node:latest AS build

WORKDIR /usr/local/app
COPY ./ /usr/local/app

RUN yarn
RUN npm run build

# Étape 2 : Serveur Nginx pour servir l'application Angular
FROM nginx:latest as production

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/local/app/dist/front /usr/share/nginx/html

EXPOSE 80