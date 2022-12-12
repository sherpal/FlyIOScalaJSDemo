package express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Compression {
  @js.native
  @JSImport("compression", JSImport.Default)
  def apply(): Middleware = js.native
}
