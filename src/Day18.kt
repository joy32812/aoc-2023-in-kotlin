import java.util.LinkedList

enum class Direction(val dx: Long, val dy: Long) {
  U(-1, 0),
  D(1, 0),
  L(0, -1),
  R(0, 1),
}

fun main() {

  data class Command(
    val dir: Direction,
    val len: Long
  )

  data class State(
    val sx: Long,
    val sy: Long,
    val ex: Long,
    val ey: Long,
  )

  fun String.toCommand1(): Command {
      val splits = this.split(" ")
      val dir = when (splits[0]) {
        "U" -> Direction.U
        "D" -> Direction.D
        "L" -> Direction.L
        else -> Direction.R
      }

      val len = splits[1].toLong()
      return Command(dir, len)
  }

  fun String.toCommand2(): Command {
    val color = this.split(" ")[2].filter { it !in "()#" }

    val dir = when(color.last()) {
      '0' -> Direction.R
      '1' -> Direction.D
      '2' -> Direction.L
      else -> Direction.U
    }

    val len = color.substring(0, color.length - 1).toLong(16)
    return Command(dir, len)
  }

  fun List<String>.toCommandList1() = this.map { it.toCommand1() }
  fun List<String>.toCommandList2() = this.map { it.toCommand2() }

  fun generateStates(possibleX: List<Long>, possibleY: List<Long>): List<State> {
    val states = mutableListOf<State>()

    fun addOne(sx: Long, sy: Long, ex: Long, ey: Long) {
      if (sx <= ex && sy <= ey) {
        states += State(sx, sy, ex, ey)
      }
    }

    for (i in possibleX.indices) {
      for (j in possibleY.indices) {
        val x = possibleX[i]
        val y = possibleY[j]

        states += State(x, y, x, y)

        if (i + 1 < possibleX.size) {
          addOne(x + 1, y, possibleX[i + 1] - 1, y)
        }

        if (j + 1 < possibleY.size) {
          addOne(x, y + 1, x, possibleY[j + 1] - 1)
        }

        if (i + 1 < possibleX.size && j + 1 < possibleY.size) {
          addOne(
            x + 1,
            y + 1,
            possibleX[i + 1] - 1,
            possibleY[j + 1] - 1
          )
        }
      }
    }

    return states
  }

  fun List<Command>.onEdge(s: State): Boolean {
    var x = 0L
    var y = 0L

    for ((dir, len) in this) {
      val nx = x + dir.dx * len
      val ny = y + dir.dy * len

      val sortX = listOf(x, nx).sorted()
      val sortY = listOf(y, ny).sorted()

      if (s.sx >= sortX[0] && s.ex <= sortX[1] && s.sy >= sortY[0] && s.ey <= sortY[1]) {
        return true
      }

      x = nx
      y = ny
    }

    return false
  }

  fun List<Command>.toPossibleXY(): Pair<List<Long>, List<Long>> {
    var x = 0L
    var y = 0L

    val possibleX = mutableSetOf(x)
    val possibleY = mutableSetOf(y)

    for ((dir, len) in this) {
      x += dir.dx * len
      y += dir.dy * len

      possibleX += x
      possibleY += y
    }

    possibleX += listOf(possibleX.min() - 1, possibleX.max() + 1)
    possibleY += listOf(possibleY.min() - 1, possibleY.max() + 1)
    return possibleX.sorted() to possibleY.sorted()
  }

  fun State.isNeighbor(other: State): Boolean {
    // up
    if (this.sx == other.ex + 1 && this.sy == other.sy) return true

    // right
    if (this.sx == other.sx && this.sy + 1 == other.sy) return true

    // left
    if (this.sx == other.sx && this.sy == other.ey + 1) return true

    // down
    if (this.ex + 1 == other.sx && this.sy == other.sy) return true

    return false
  }

  fun List<State>.toAdjMap(): Map<Int, MutableSet<Int>> {
    val adj = mutableMapOf<Int, MutableSet<Int>>()

    for (i in this.indices) {
      for (j in i + 1 until this.size) {
        if (this[i].isNeighbor(this[j]) || this[j].isNeighbor(this[i])) {
          adj.getOrPut(i) { mutableSetOf() } += j
          adj.getOrPut(j) { mutableSetOf() } += i
        }
      }
    }

    return adj
  }

  fun State.toAreaSize() = (this.ex - this.sx + 1) * (this.ey - this.sy + 1)
  fun List<Command>.bfs(): Long {
    val (possibleX, possibleY) = this.toPossibleXY()

    val states = generateStates(possibleX, possibleY)

    val edgeSet = states.withIndex().filter { this.onEdge(it.value) }.map { it.index }.toSet()
    val adjMap = states.toAdjMap()

    val Q = LinkedList<Int>().apply { add(0) }
    val visited = mutableSetOf(0)

    while (Q.isNotEmpty()) {
      val i = Q.poll()

      val adj = adjMap[i] ?: emptySet()
      for (j in adj) {
        if (j in edgeSet) continue
        if (j in visited) continue

        visited += j
        Q += j
      }
    }
    val total = (possibleX.last() - possibleX.first() + 1) * (possibleY.last() - possibleY.first() + 1)
    val visitedSize = visited.sumOf { i -> states[i].toAreaSize() }

    return total - visitedSize
  }

  fun part1(input: List<String>): Long {
    return input.toCommandList1().bfs()
  }
  fun part2(input: List<String>): Long {
    return input.toCommandList2().bfs()
  }

  val testInput = readInput("Day18_test")
  check(part1(testInput) == 62L)
  check(part2(testInput) == 952408144115L)

  val input = readInput("Day18")
  part1(input).println()
  part2(input).println()
}