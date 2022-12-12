import scala.sys.process.Process

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.2.0"

val circeVersion = "0.14.1"

ThisBuild / scalacOptions ++= Seq(          // use ++= to add to existing options
  "-encoding", "utf8",          // if an option takes an arg, supply it on the same line
  "-feature",                   // then put the next option on a new line for easy editing
  "-language:implicitConversions",
  "-language:existentials",
  "-unchecked",
  "-Werror"
)

lazy val `shared-logic` = project
  .in(file("./shared-logic"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    // circe for backend<>frontend communication
    libraryDependencies ++= List(
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser"
    ).map(_ % circeVersion)
  )

def esModule = Def.settings(scalaJSLinkerConfig ~= {
  _.withModuleKind(ModuleKind.ESModule)
})

lazy val server = project
  .in(file("./server"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= List(
    ),
    esModule,
    scalaJSUseMainModuleInitializer := true,
    Compile / fullLinkJS / scalaJSLinkerOutputDirectory := baseDirectory.value / ".." / "deploy" / "dist" / "server"
  )
  .dependsOn(`shared-logic`)

lazy val frontend = project
  .in(file("./frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= List(
      // web framework (other choices are slinky, scala-js-react, outwatch...)
      "com.raquo" %%% "laminar" % "0.14.5",
      // web component library (other (non-exclusive) choices are material-ui, bootstrap...)
      "be.doeraene" %%% "web-components-ui5" % "1.8.0"
    ),
    esModule,
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(`shared-logic`)

val buildFrontend = taskKey[Unit]("Build frontend")

buildFrontend := {
  /*
  To build the frontend, we do the following things:
  - fullLinkJS the frontend sub-module
  - run npm ci in the frontend directory (might not be required)
  - package the application with vite-js (output will be in the resources of the server sub-module)
   */
  (frontend / Compile / fullLinkJS).value
  val npmCiExit = Process(Utils.npm :: "ci" :: Nil, cwd = baseDirectory.value / "frontend").run().exitValue()
  if (npmCiExit > 0) {
    throw new IllegalStateException(s"npm ci failed. See above for reason")
  }

  val buildExit = Process(Utils.npm :: "run" :: "build" :: Nil, cwd = baseDirectory.value / "frontend").run().exitValue()
  if (buildExit > 0) {
    throw new IllegalStateException(s"Building frontend failed. See above for reason")
  }

  IO.copyDirectory(baseDirectory.value / "frontend" / "dist", baseDirectory.value / "deploy" / "dist" / "static" / "static")
}

val copyNpmServerData = Def.task {
  IO.copyFile(file("./server/package.json"), file("./deploy/dist/package.json"))
  IO.copyFile(file("./server/package-lock.json"), file("./deploy/dist/package-lock.json"))
}

val packageApplication = taskKey[Unit]("Package the all application in dist folder")

packageApplication := {
  buildFrontend.value
  copyNpmServerData.value
  (server / Compile / fullLinkJS).value
  streams.value.log.info("Done")
}
