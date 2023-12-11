fun main() {
  data class Galaxy(
    val x: Int,
    val y: Int
  )

  fun List<String>.toGalaxyList(): List<Galaxy> {
    val result = mutableListOf<Galaxy>()
    for (i in this.indices) {
      for (j in this[i].indices) {
        if (this[i][j] == '#') result += Galaxy(i, j)
      }
    }
    return result
  }

  fun List<String>.toEmptyRowsAndCols(): Pair<Set<Int>, Set<Int>> {
    val n = this[0].length
    val rows = Array(n) {0}
    val cols = Array(n) {0}
    for (i in this.indices) {
      for (j in this[i].indices) {
        if (this[i][j] == '#') {
          rows[i] ++
          cols[j] ++
        }
      }
    }

    val rowSet = rows.indices.filter { i -> rows[i] == 0 }.toSet()
    val colSet = rows.indices.filter { i -> cols[i] == 0 }.toSet()

    return rowSet to colSet
  }

  fun solve(input: List<String>, times: Long): Long {
    val galaxies = input.toGalaxyList()
    val (rowSet, colSet) = input.toEmptyRowsAndCols()

    var ans = 0L
    for (i in galaxies.indices) {
      for (j in i + 1 until galaxies.size) {

        val xRange = listOf(galaxies[i].x, galaxies[j].x).sorted()
        val yRange = listOf(galaxies[i].y, galaxies[j].y).sorted()

        val xDis = (xRange[0] until xRange[1]).map { if (it in rowSet) times else 1L }.sum()
        val yDis = (yRange[0] until yRange[1]).map { if (it in colSet) times else 1L }.sum()

        ans += xDis + yDis
      }
    }

    return ans
  }

  fun part1(input: List<String>): Long {
    return solve(input, 2)
  }

  fun part2(input: List<String>): Long {
    return solve(input, 1000000)
  }

  val input = readInput("Day11")
  part1(input).println()
  part2(input).println()
}