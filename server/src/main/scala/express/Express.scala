package express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Express extends js.Object {
  def get(path: String, fn: js.Function2[Request, Response, Unit]): js.Object = js.native

  def post(path: String, fn: js.Function2[Request, Response, Unit]): js.Object = js.native

  def listen(port: Int, host: String, fn: js.Function0[Unit]): Unit = js.native

  def use(middleware: Middleware): Unit = js.native

  def use(path: String, middleware: Middleware): Unit = js.native
}

object Express {
  @js.native
  @JSImport("express", JSImport.Default)
  def apply(): Express = js.native

  @js.native
  @JSImport("express", "static")
  def static(root: String): Middleware = js.native

  @js.native
  @JSImport("express", "json")
  def json(): Middleware = js.native

  extension (express: Express)
    def useInProduction(middleware: => Middleware): Unit =
      if scala.scalajs.LinkingInfo.productionMode then express.use(middleware)
}
