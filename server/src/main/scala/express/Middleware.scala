package express

import scala.scalajs.js

trait Middleware extends js.Object

object Middleware {

  given Conversion[(Request, Response, () => Unit) => Unit, Middleware] with {
    override def apply(x: (Request, Response, () => Unit) => Unit): Middleware =
      (((req: Request, res: Response, next: js.Function0[Unit]) => x(req, res, next)): js.Function3[
        Request,
        Response,
        js.Function0[Unit],
        Unit
      ]).asInstanceOf[Middleware]
  }

  def fromFunction(fn: (Request, Response, () => Unit) => Unit): Middleware = fn

}
