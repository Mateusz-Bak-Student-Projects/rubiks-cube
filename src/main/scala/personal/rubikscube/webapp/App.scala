package personal.rubikscube.webapp

import personal.rubikscube.domain.cube.implementation.CubeImpl
import personal.rubikscube.domain.cube.model.{Color, Cube, Face}
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{HTMLElement, Node}
import personal.rubikscube.domain.cube.model.Cube.FaceMatrix

object App {

  val cubeDisplay = Map(
    Face.Front -> cubeFace("front"),
    Face.Back -> cubeFace("back"),
    Face.Up -> cubeFace("up"),
    Face.Down -> cubeFace("down"),
    Face.Left -> cubeFace("left"),
    Face.Right -> cubeFace("right")
  )

  def run: Unit = {
    val cube = CubeImpl.solved
    updateDisplay(cube)
  }

  private def updateDisplay(cube: Cube): Unit = {
    cubeDisplay.foreach { (face, node) =>
      val faceMatrix = cube.getFace(face)
      val colors = colorClassNames(faceMatrix)
      val rows = node.childNodes
      for (i <- 0 to 2) {
        val cells = rows(i).childNodes
        for (j <- 0 to 2) {
          if (cells(j).isInstanceOf[HTMLElement]) {
            val cell = cells(j).asInstanceOf[HTMLElement]
            cell.className = colors(i)(j)
          }
        }
      }
    }
  }

  private def colorClassNames(
      faceMatrix: FaceMatrix
  ): IndexedSeq[IndexedSeq[String]] =
    IndexedSeq(
      IndexedSeq(
        colorClassName(faceMatrix._1._1),
        colorClassName(faceMatrix._1._2),
        colorClassName(faceMatrix._1._3)
      ),
      IndexedSeq(
        colorClassName(faceMatrix._2._1),
        colorClassName(faceMatrix._2._2),
        colorClassName(faceMatrix._2._3)
      ),
      IndexedSeq(
        colorClassName(faceMatrix._3._1),
        colorClassName(faceMatrix._3._2),
        colorClassName(faceMatrix._3._3)
      )
    )

  private def colorClassName(color: Color): String =
    color match {
      case Color.Red    => "red"
      case Color.Orange => "orange"
      case Color.White  => "white"
      case Color.Yellow => "yellow"
      case Color.Green  => "green"
      case Color.Blue   => "blue"
    }

  private def cubeFace(name: String): Node = {
    val table = document.createElement("table")
    table.classList.add(name)
    for (i <- 1 to 3) {
      val row = document.createElement("tr")
      for (j <- 1 to 3) {
        val cell = document.createElement("td")
        row.appendChild(cell)
      }
      table.appendChild(row)
    }
    document.getElementById("container").appendChild(table)
  }
}
