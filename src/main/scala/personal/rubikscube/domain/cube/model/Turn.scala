package personal.rubikscube.domain.cube.model

private[cube] enum Turn {
  case Clockwise, Counterclockwise, HalfTurn
  
  def reverse = this match {
    case Clockwise        => Counterclockwise
    case HalfTurn         => HalfTurn
    case Counterclockwise => Clockwise
  }

  def clockwiseTurns = this match {
    case Clockwise        => 1
    case HalfTurn         => 2
    case Counterclockwise => 3
  }
}
