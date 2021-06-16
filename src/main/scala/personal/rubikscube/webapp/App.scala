package personal.rubikscube.webapp

import personal.rubikscube.domain.cube.implementation.CubeImpl
import personal.rubikscube.domain.cube.model.{Color, Cube, Face, Turn, Axis}
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.{HTMLElement, HTMLImageElement, Node}
import personal.rubikscube.domain.cube.model.Cube.FaceMatrix

object App {

  val cubeDisplay = Map(
    Face.Front -> cubeFaceUI("front", Face.Front),
    Face.Back -> cubeFaceUI("back", Face.Back),
    Face.Up -> cubeFaceUI("up", Face.Up),
    Face.Down -> cubeFaceUI("down", Face.Down),
    Face.Left -> cubeFaceUI("left", Face.Left),
    Face.Right -> cubeFaceUI("right", Face.Right)
  )
  var cube = CubeImpl.solved

  def run: Unit = {
    cubeGlobalUI
    updateDisplay
  }

  private def updateDisplay: Unit = {
    cubeDisplay.foreach { (face, node) =>
      val faceMatrix = cube.getFace(face)
      val colors = colorClassNames(faceMatrix)
      val rows = node.childNodes
      for (i <- 0 to 2) {
        val cells = rows(i).childNodes
        for (j <- 0 to 2) {
          val cell = cells(j).asInstanceOf[HTMLElement]
          cell.className = colors(i)(j)
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

  private def cubeFaceUI(name: String, face: Face): Node = {
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

    val clockwiseButton =
      document.createElement("img").asInstanceOf[HTMLImageElement]
    clockwiseButton.classList.add(name)
    clockwiseButton.src = "clockwise.png"
    clockwiseButton.addEventListener(
      "click",
      { (e: dom.MouseEvent) =>
        cube = cube.applyMove(face, Turn.Clockwise)
        updateDisplay
      }
    )

    val counterclockwiseButton =
      document.createElement("img").asInstanceOf[HTMLImageElement]
    counterclockwiseButton.classList.add(name)
    counterclockwiseButton.src = "counterclockwise.png"
    counterclockwiseButton.addEventListener(
      "click",
      { (e: dom.MouseEvent) =>
        cube = cube.applyMove(face, Turn.Counterclockwise)
        updateDisplay
      }
    )

    document.getElementById("buttons-clockwise").appendChild(clockwiseButton)
    document
      .getElementById("buttons-counterclockwise")
      .appendChild(counterclockwiseButton)
    document.getElementById("container").appendChild(table)
  }

  def cubeGlobalUI: Unit = {
    for (axis <- Axis.values) {
      val div = document.createElement("div")
      div.textContent = axis.toString
      val clockwiseButton =
        document.createElement("img").asInstanceOf[HTMLImageElement]
      clockwiseButton.src = "clockwise.png"
      clockwiseButton.addEventListener(
        "click",
        { (e: dom.MouseEvent) =>
          cube = cube.applyMove(axis, Turn.Clockwise)
          updateDisplay
        }
      )
      val counterclockwiseButton =
        document.createElement("img").asInstanceOf[HTMLImageElement]
      counterclockwiseButton.src = "counterclockwise.png"
      counterclockwiseButton.addEventListener(
        "click",
        { (e: dom.MouseEvent) =>
          cube = cube.applyMove(axis, Turn.Counterclockwise)
          updateDisplay
        }
      )
      div.appendChild(clockwiseButton)
      div.appendChild(counterclockwiseButton)
      document.getElementById("global-cube-buttons").appendChild(div)
    }
    val shuffleButton = document.createElement("button")
    shuffleButton.textContent = "Shuffle"
    shuffleButton.addEventListener(
      "click",
      { (e: dom.MouseEvent) =>
        cube = cube.shuffle
        updateDisplay
      }
    )
    document.getElementById("global-cube-buttons").appendChild(shuffleButton)
    val resetButton = document.createElement("button")
    resetButton.textContent = "Reset"
    resetButton.addEventListener(
      "click",
      { (e: dom.MouseEvent) =>
        cube = CubeImpl.solved
        updateDisplay
      }
    )
    document.getElementById("global-cube-buttons").appendChild(resetButton)
  }
}
