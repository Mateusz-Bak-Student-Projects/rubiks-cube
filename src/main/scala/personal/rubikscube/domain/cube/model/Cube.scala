package personal.rubikscube.domain.cube.model

import personal.rubikscube.domain.cube.model.Cube.FaceMatrix

trait Cube {

  def getFace(face: Face): FaceMatrix

  def applyMove(target: Face | Axis, turn: Turn): Cube
}

object Cube {
  //@formatter:off
  /**
   * Face layout:
   *   U
   * L F R
   *   D
   *   B
   */

  type FaceMatrix = (
      (Color, Color, Color),
      (Color, Color, Color),
      (Color, Color, Color)
    )
  //@formatter:on  
}
