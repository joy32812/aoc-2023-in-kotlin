import java.util.LinkedList

enum class Direction(val dx: Int, val dy: Int) {
  U(-1, 0),
  D(1, 0),
  L(0, -1),
  R(0, 1),
}
fun main() {

  data class Command(
    val dir: Direction,
    val cnt: Int,
    val color: String
  )

  data class Index(val x: Int, val y: Int)

  fun String.toCommand(): Command {
    val splits = this.split(" ")
    val dir = when (splits[0]) {
      "U" -> Direction.U
      "D" -> Direction.D
      "L" -> Direction.L
      else -> Direction.R
    }

    val cnt = splits[1].toInt()
    val color = splits[2].filter { it !in "()" }
    return Command(dir, cnt, color)
  }

  fun List<String>.toCommands() = this.map { it.toCommand() }

  fun part1(input: List<String>): Int {
    val commands = input.toCommands()

    val init = Index(0, 0)
    val edges = mutableSetOf<Index>().apply { add(init) }

    var minX = 0
    var maxX = 0
    var minY = 0
    var maxY = 0

    var x = 0
    var y = 0

    for ((dir, cnt, _) in commands) {
      for (k in 1 .. cnt) {
        x += dir.dx
        y += dir.dy

        edges += Index(x, y)

        minX = minOf(minX, x)
        maxX = maxOf(maxX, x)
        minY = minOf(minY, y)
        maxY = maxOf(maxY, y)
      }
    }

    // BFS
    val xRange = minX - 1 .. maxX + 1
    val yRange = minY - 1 .. maxY + 1
    val dx = listOf(0, 1, 0, -1)
    val dy = listOf(1, 0, -1, 0)

    val first = Index(xRange.first, yRange.first)
    val visited = mutableSetOf<Index>().apply { add(first) }
    val Q = LinkedList<Index>().apply { add(first) }

    while (Q.isNotEmpty()) {
      val (cx, cy) = Q.poll()

      for (k in dx.indices) {
        val nx = cx + dx[k]
        val ny = cy + dy[k]
        val nIndex = Index(nx, ny)

        if (nx !in xRange || ny !in yRange) continue
        if (nIndex in edges) continue
        if (nIndex in visited) continue

        visited += nIndex
        Q += nIndex
      }
    }

    return (xRange.last - xRange.first + 1) * (yRange.last - yRange.first + 1) - visited.size
  }

  fun part2(input: List<String>): Int {
    return 1
  }

  val testInput = readInput("Day18_test")
  check(part1(testInput) == 62)
  check(part2(testInput) == 1)

  val input = readInput("Day18")
  part1(input).println()
  part2(input).println()
}