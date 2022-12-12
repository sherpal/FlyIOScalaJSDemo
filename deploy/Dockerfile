FROM node:16-alpine

COPY ./dist/. /app/.
WORKDIR /app

RUN npm ci

EXPOSE 8080

ENV NODE_ENV=production

ENTRYPOINT [ "npm", "run", "prod" ]
