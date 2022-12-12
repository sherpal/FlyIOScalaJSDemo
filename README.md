# Full Stack Scala (with Node Backend) on fly.io demo

This repository is an example of an application with backend and frontend both in Scala that can be deployed easily on [fly.io](https://fly.io/).

It features:

- frontend with [Laminar](https://laminar.dev/)
- frontend web-component library with [UI5](https://sap.github.io/ui5-webcomponents/)
- frontend packaging with [vitejs](https://vitejs.dev/)
- backend (on NodeJS) with [express](https://expressjs.com/)
- backend hot reload with [nodemon](https://www.npmjs.com/package/nodemon)
- JSON serialization with [circe](https://circe.github.io/circe/)

> If you prefer an example with a backend on the JVM, see [this repo](https://github.com/sherpal/FlyIOScalaJVMDemo) instead.

## Dev setup

To run this project, you will need installed

- npm (v16)
- sbt
- a jdk 11

The first time, run in one terminal:

```
cd frontend
npm ci
cd ..
cd server
npm ci
```

Then, in one terminal:

```
sbt
~fastLinkJS
```

(If you prefer to separate backend and frontend compilation, you can do `~server/fastLinkJS` and `~frontend/fastLinkJS` in two different terminals.)

In another terminal

```
cd server
npm run dev
```

In a third (last) terminal

```
cd frontend
npm run dev
```

Then you can go to [`localhost:3000/static`](http://localhost:3000/static).

## Prod setup

You can package the whole application into the `deploy` folder with

```
sbt packageApplication
```

Then you can run it with `cd deploy/dist && npm ci && npm run prod` then go to [`localhost:9000`](http://localhost:9000).

You can also build a Docker image for the app with (note that the tag name does not matter)

```
cd deploy
docker build --tag demoscalaflyio .
```

Then run it with

```
docker run --rm -p 9000:8080 demoscalaflyio
```

and then go to [`localhost:9000`](http://localhost:9000).

## Deploy to fly.io

The project contains a `fly.toml` file in the `deploy` directory generated via `fly launch`.

You can apply it to your account via `fly deploy` and then see that it's live with `fly open`. (If not logged in already, you need to `fly auth login`.)

You need to have [flyctl](https://fly.io/docs/hands-on/install-flyctl/) installed for this to work.

You can see more complete instructions [here](https://fly.io/docs/languages-and-frameworks/dockerfile/).
