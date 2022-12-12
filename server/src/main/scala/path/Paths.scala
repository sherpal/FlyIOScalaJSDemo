package path

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("path", JSImport.Namespace)
object Paths extends js.Object {

  def resolve(path: Path): Path = js.native
  
  def join(paths: Path*): Path = js.native

}
