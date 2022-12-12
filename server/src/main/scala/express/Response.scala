package express

import path.Path

import scala.scalajs.js

@js.native
trait Response extends js.Object {
  
  def send(message: String): Unit = js.native
  
  def sendFile(path: Path): Unit = js.native
  
  def status(code: Int): this.type = js.native

}
