import java.util.LinkedList

fun main() {
  data class State(
    val x: Int,
    val y: Int,
    val from: Int,
    val cnt: Int
  )

  // left, up, right, down
  val dx = listOf(0, 1, 0, -1)
  val dy = listOf(-1, 0, 1, 0)

  fun List<String>.bfs(okayRange: IntRange): Int {
    val n = size
    val m = this[0].length
    var ans = Int.MAX_VALUE

    val init = State(0, 0, -1, 0)

    val ansMap = mutableMapOf<State, Int>().apply { put(init, 0) }
    val Q = LinkedList<State>().apply { add(init) }
    val inQ = mutableSetOf<State>().apply { add(init) }

    while (Q.isNotEmpty()) {
      val cur = Q.poll()
      inQ.remove(cur)

      val (x, y, from, cnt) = cur
      val curDist = ansMap[cur]!!

      val reversedDir = if (from == -1) -1 else (from + 2) % 4

      for (k in dx.indices) {
        if (k == reversedDir) continue

        var sum = 0
        for (z in 1 .. okayRange.last) {
          val nx = x + z * dx[k]
          val ny = y + z * dy[k]

          val nCnt = if (k == from) cnt + z else z
          val nState = State(nx, ny, k, nCnt)

          if (nx !in 0 until n || ny !in 0 until m) continue
          sum += this[nx][ny] - '0'

          if (nCnt !in okayRange) continue
          val nDis = curDist + sum

          if (nState !in ansMap || nDis < ansMap[nState]!!) {
            ansMap[nState] = nDis
            if (nState !in inQ) {
              Q.add(nState)
              inQ.add(nState)
            }
          }

          if (nx == n - 1 && ny == m - 1) {
            ans = minOf(ans, nDis)
          }
        }
      }
    }

    return ans
  }

  fun part1(input: List<String>): Int {
    return input.bfs(1..3)
  }

  fun part2(input: List<String>): Int {
    return input.bfs(4..10)
  }

  val testInput = readInput("Day17_test")
  check(part1(testInput) == 102)
  check(part2(testInput) == 94)

  val input = readInput("Day17")
  part1(input).println()
  part2(input).println()
}
