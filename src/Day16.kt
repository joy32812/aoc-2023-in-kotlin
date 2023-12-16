import java.util.LinkedList

//enum class Direction {
//  TO_UP,
//  TO_DOWN,
//  TO_LEFT,
//  TO_RIGHT
//}
//
fun main() {

  // Direction
    // 0: up
    // 1: down
    // 2: left
    // 3: right
  data class State(val x: Int, val y: Int, val dir: Int)

  fun State.toNextStates(ch: Char): List<State> {

    return when (ch) {
      '|' -> {
        when (dir) {
          0 -> listOf(State(x - 1, y, dir))
          1 -> listOf(State(x + 1, y, dir))
          else -> listOf(State(x - 1, y, 0), State(x + 1, y, 1))
        }
      }
      '-' -> {
        when (dir) {
          2 -> listOf(State(x, y - 1, dir))
          3 -> listOf(State(x, y + 1, dir))
          else -> listOf(State(x, y - 1, 2), State(x, y + 1, 3))
        }
      }
      '/' -> {
        when (dir) {
          0 -> listOf(State(x, y + 1, 3))
          1 -> listOf(State(x, y - 1, 2))
          2 -> listOf(State(x + 1, y, 1))
          3 -> listOf(State(x - 1, y, 0))
          else -> listOf()
        }
      }
      '\\' -> {
        when (dir) {
          0 -> listOf(State(x, y - 1, 2))
          1 -> listOf(State(x, y + 1, 3))
          2 -> listOf(State(x - 1, y, 0))
          3 -> listOf(State(x + 1, y, 1))
          else -> listOf()
        }
      }
      else -> {
        when (dir) {
          0 -> listOf(State(x - 1, y, dir))
          1 -> listOf(State(x + 1, y, dir))
          2 -> listOf(State(x, y - 1, dir))
          3 -> listOf(State(x, y + 1, dir))
          else -> listOf()
        }
      }
    }
  }

  fun List<String>.bfs(init: State): Int {
    val n = this.size
    val m = this[0].length

    val visited = Array(n) { Array(m) { BooleanArray(4) } }
    visited[init.x][init.y][init.dir] = true
    val Q = LinkedList<State>().apply { add(init) }

    while (Q.isNotEmpty()) {
      val curState = Q.poll()

      val nextStates = curState.toNextStates(this[curState.x][curState.y])

      for (state in nextStates) {
        if (state.x !in 0 until n || state.y !in 0 until m) continue

        if (visited[state.x][state.y][state.dir]) continue
        visited[state.x][state.y][state.dir] = true
        Q.add(state)
      }
    }

    var ans = 0
    for (i in 0 until n) {
      for (j in 0 until m) {
        if (visited[i][j].any { it }) ans++
      }
    }

    return ans
  }

  // BFS
  fun part1(input: List<String>): Int {
    return input.bfs(State(0, 0, 3))
  }

  fun part2(input: List<String>): Int {
    val n = input.size
    val m = input[0].length
    var ans = 0

    // down
    for (j in 0 until m) {
      ans = maxOf(ans, input.bfs(State(0, j, 1)))
    }

    // right
    for (i in 0 until n) {
      ans = maxOf(ans, input.bfs(State(i, 0, 3)))
    }

    // up
    for (j in 0 until m) {
      ans = maxOf(ans, input.bfs(State(n - 1, j, 0)))
    }

    // left
    for (i in 0 until n) {
      ans = maxOf(ans, input.bfs(State(i, m - 1, 2)))
    }

    return ans
  }

  val testInput = readInput("Day16_test")
  check(part1(testInput) == 46)
  check(part2(testInput) == 51)

  val input = readInput("Day16")
  part1(input).println()
  part2(input).println()
}
