package personal.rubikscube.domain.cube.implementation

import personal.rubikscube.domain.cube.model.Face

private object LayerMask {

  private type Mask = List[Boolean]

  private val top = mask(List(0, 1, 1, 0, 0, 0, 0, 0, 1))
  private val bottom = mask(List(0, 0, 0, 0, 1, 1, 1, 0, 0))
  private val left = mask(List(0, 0, 0, 0, 0, 0, 1, 1, 1))
  private val right = mask(List(0, 0, 1, 1, 1, 0, 0, 0, 0))
  private val full = mask(List.fill(9)(1))
  private val empty = mask(List.fill(9)(0))

  def get(face: Face): Map[Face, Mask] =
    face match {
      case Face.Front => mask(full, empty, bottom, top, right, left)
      case Face.Back  => mask(empty, full, top, bottom, left, right)
      case Face.Up    => mask(top, bottom, full, empty, top, top)
      case Face.Down  => mask(bottom, top, empty, full, bottom, bottom)
      case Face.Left  => mask(left, left, left, left, full, empty)
      case Face.Right => mask(right, right, right, right, empty, full)
    }

  private def mask(
      front: Mask,
      back: Mask,
      up: Mask,
      down: Mask,
      left: Mask,
      right: Mask
  ): Map[Face, Mask] =
    Map(
      Face.Front -> front,
      Face.Back -> back,
      Face.Up -> up,
      Face.Down -> down,
      Face.Left -> left,
      Face.Right -> right
    )

  private def mask(list: List[Int]): Mask = {
    assert(list.size == 9)
    list.map {
      case 0 => false
      case _ => true
    }
  }
}
