package personal.rubikscube.util

extension[A](list: List[A]) {
  def rotateRight(i: Int): List[A] = {
    val size = list.size
    (list.view.drop(size - (i % size)) ++ list.view.take(size - (i % size))).toList
  }
}
