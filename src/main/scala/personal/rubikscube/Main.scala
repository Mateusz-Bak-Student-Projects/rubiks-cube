package personal.rubikscube

import org.scalajs.dom
import org.scalajs.dom.document

@main def hello: Unit =
  println("Hello world!")
  println(msg)
  appendPar(document.body, "Hello World")

def msg = "I was compiled by Scala 3. :)"

def appendPar(targetNode: dom.Node, text: String): Unit = {
  val parNode = document.createElement("p")
  parNode.textContent = text
  targetNode.appendChild(parNode)
}
