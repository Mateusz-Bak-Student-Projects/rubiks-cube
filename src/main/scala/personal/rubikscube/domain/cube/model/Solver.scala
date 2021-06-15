package personal.rubikscube.domain.cube.model

private[cube] trait Solver {

  protected type Move = Cube => Cube

  def solve(cube: Cube): List[Move]

  protected def move(target: Face | Axis, turn: Turn)(cube: Cube): Cube =
    cube.applyMove(target, turn)
}
