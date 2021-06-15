package personal.rubikscube.domain.cube.model

import personal.rubikscube.domain.cube.model.Cube.FaceMatrix

private[cube] trait Cube {

  def getFace(face: Face): FaceMatrix

  def applyMove(target: Face | Axis, turn: Turn): Cube

  def solved: Cube
}

private[cube] object Cube {
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
