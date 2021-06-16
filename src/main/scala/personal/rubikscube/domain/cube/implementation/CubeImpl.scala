package personal.rubikscube.domain.cube.implementation

import personal.rubikscube.domain.cube.implementation.CubeImpl.{
  FaceState,
  FaceTransform,
  faceMatrix,
  transform
}
import personal.rubikscube.domain.cube.model.Cube.FaceMatrix
import personal.rubikscube.domain.cube.model.{Axis, Color, Cube, Face, Turn}
import personal.rubikscube.util.rotateRight

import scala.collection.immutable.Nil

private class CubeImpl private (private val state: Map[Face, FaceState])
    extends Cube {

  override def getFace(face: Face): FaceMatrix =
    faceMatrix(getFaceState(face))

  override def applyMove(target: Face | Axis, turn: Turn): CubeImpl =
    target match {
      case face: Face => rotateLayer(face, turn)
      case axis: Axis => rotateCube(axis, turn)
    }

  override def solved: CubeImpl =
    CubeImpl(
      Map(
        Face.Front -> solvedFace(Color.Red),
        Face.Back -> solvedFace(Color.Orange),
        Face.Up -> solvedFace(Color.White),
        Face.Down -> solvedFace(Color.Yellow),
        Face.Left -> solvedFace(Color.Green),
        Face.Right -> solvedFace(Color.Blue)
      )
    )

  private def solvedFace(color: Color): FaceState = List.fill(9)(color)

  private def rotateLayer(face: Face, turn: Turn): CubeImpl = {
    val rotatedCube = face match {
      case Face.Front => rotateCube(Axis.Z, turn)
      case Face.Back  => rotateCube(Axis.Z, turn.reverse)
      case Face.Up    => rotateCube(Axis.Y, turn)
      case Face.Down  => rotateCube(Axis.Y, turn.reverse)
      case Face.Left  => rotateCube(Axis.X, turn.reverse)
      case Face.Right => rotateCube(Axis.X, turn)
    }
    combine(rotatedCube, face)
  }

  private def rotateCube(axis: Axis, turn: Turn): CubeImpl = {
    val quarterTurn = axis match {
      case Axis.X =>
        transform(
          front = setFace(Face.Down),
          back = setFace(Face.Up),
          up = setFace(Face.Front),
          down = setFace(Face.Back),
          left = rotateFace(3),
          right = rotateFace(1)
        )
      case Axis.Y =>
        transform(
          front = setFace(Face.Right),
          back = setAndRotateFace(Face.Left, 2),
          up = rotateFace(1),
          down = rotateFace(3),
          left = setFace(Face.Front),
          right = setAndRotateFace(Face.Back, 2)
        )
      case Axis.Z =>
        transform(
          front = rotateFace(1),
          back = rotateFace(4),
          up = setAndRotateFace(Face.Left, 3),
          down = setAndRotateFace(Face.Right, 3),
          left = setAndRotateFace(Face.Up, 3),
          right = setAndRotateFace(Face.Down, 3)
        )
    }
    (1 to turn.clockwiseTurns).foldLeft(this)((x, _) => quarterTurn(x))
  }

  private def setFace(
      target: Face
  )(faceState: FaceState): FaceState =
    getFaceState(target)

  private def rotateFace(
      clockwiseTurns: Int
  )(faceState: FaceState): FaceState = {
    val center :: border = faceState
    center :: (border.rotateRight(clockwiseTurns * 2))
  }

  private def setAndRotateFace(
      target: Face,
      clockwiseTurns: Int
  )(faceState: FaceState): FaceState =
    rotateFace(clockwiseTurns)(setFace(target)(faceState))

  private def combine(other: CubeImpl, layerMask: Face): CubeImpl =
    CubeImpl(LayerMask.get(layerMask).map { (face, mask) =>
      val primary = getFaceState(face)
      val secondary = other.getFaceState(face)
      val combined = primary.zip(secondary).zip(mask).map {
        case ((p, s), true)  => s
        case ((p, s), false) => p
      }
      face -> combined
    })

  private def getFaceState(face: Face): FaceState = state.get(face).get
}

object CubeImpl {

  /**
    * Face indices:
    * 8 1 2
    * 7 0 3
    * 6 5 4
    */
  private type FaceState = List[Color]

  private type FaceTransform = FaceState => FaceState

  private def faceMatrix(faceState: FaceState): FaceMatrix = {
    val t0 :: t1 :: t2 :: t3 :: t4 :: t5 :: t6 :: t7 :: t8 :: Nil = faceState
    (
      (t8, t1, t2),
      (t7, t0, t3),
      (t6, t5, t4)
    )
  }

  private def transform(
      front: FaceTransform,
      back: FaceTransform,
      up: FaceTransform,
      down: FaceTransform,
      left: FaceTransform,
      right: FaceTransform
  )(cube: CubeImpl): CubeImpl =
    CubeImpl(
      Map(
        Face.Front -> front(cube.getFaceState(Face.Front)),
        Face.Back -> back(cube.getFaceState(Face.Back)),
        Face.Up -> up(cube.getFaceState(Face.Up)),
        Face.Down -> down(cube.getFaceState(Face.Down)),
        Face.Left -> left(cube.getFaceState(Face.Left)),
        Face.Right -> right(cube.getFaceState(Face.Right))
      )
    )
}
