import java.util.LinkedList

fun main() {

  data class Index(val x: Int, val y: Int)

  // index with the character 'S'
  fun List<String>.toStartIndex(): Index {
    for (x in indices) {
      for (y in this[x].indices) {
        if (this[x][y] == 'S') {
          return Index(x, y)
        }
      }
    }
    error("No start index found")
  }

  val dx = listOf(0, 1, 0, -1)
  val dy = listOf(-1, 0, 1, 0)

  fun part1(input: List<String>, steps: Int = 64): Int {

    val startIndex = input.toStartIndex()
    var Q = mutableSetOf(startIndex)

    repeat(steps) {
      val tmpQ = mutableSetOf<Index>()

      for ((x, y) in Q) {
        for (i in 0..3) {
          val nx = x + dx[i]
          val ny = y + dy[i]

          if (nx in input.indices && ny in input[0].indices && input[nx][ny] != '#') {
            tmpQ.add(Index(nx, ny))
          }
        }
      }

      Q = tmpQ
    }

    return Q.size
  }

  fun part2(input: List<String>): Int {
    return 1
  }

  val testInput = readInput("Day21_test")
  check(part1(testInput, 6) == 16)
  check(part2(testInput) == 1)

  val input = readInput("Day21")
  part1(input).println()
  part2(input).println()
}
