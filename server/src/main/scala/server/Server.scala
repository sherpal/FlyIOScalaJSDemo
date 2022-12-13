package server

import express.{Compression, Express, Middleware}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import io.circe.scalajs.decodeJs
import business.SomeSharedData
import path.Path
import scala.scalajs.LinkingInfo

@main def launchServer(): Unit = {
  def devProdValue[A](forDev: => A, forProd: => A): A =
    if LinkingInfo.developmentMode then forDev else forProd

  val port = devProdValue(9000, 8080)
  val host = devProdValue("localhost", "0.0.0.0")

  val app = Express()
  app.get("/", (_, res) => res.sendFile(Path.pwd :/ "static/static/index.html"))
  app.use("/api/do-thing", Express.json())
  app.post(
    "/api/do-thing",
    { (req, res) =>
      decodeJs[SomeSharedData](req.body) match {
        case Left(err)    => res.status(400).send(err.getMessage)
        case Right(value) => res.send(s"I did something very cool with your $value")
      }
    }
  )
  app.use(Express.static("static"))

  app.useInProduction(Compression())

  app.listen(
    port,
    host,
    () => {
      dom.console.log(s"Started listening to $port")
    }
  )

}
