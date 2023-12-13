fun main() {

  fun List<String>.getMirrorCol(excluded: Int = -1): Int? {
    fun okay(col: Int): Boolean {
      return this.indices.all { i ->
        val left = this[i].substring(0, col + 1).reversed()
        val right = this[i].substring(col + 1)

        left.startsWith(right) || right.startsWith(left)
      }
    }

    return (0 until this[0].length - 1).filter { it != excluded }.firstOrNull { okay(it) }
  }

  fun List<String>.getMirrorRow(excluded: Int = -1): Int? {
    fun okay(row: Int): Boolean {

      return this[0].indices.all { j ->
        val top = (0 .. row).map { i -> this[i][j] }.joinToString("").reversed()
        val bottom = (row + 1 until this.size).map { i -> this[i][j] }.joinToString("")

        top.startsWith(bottom) || bottom.startsWith(top)
      }
    }

    return (0 until this.size - 1).filter { it != excluded }.firstOrNull { okay(it) }
  }

  fun List<String>.getScore(): Int? {
    val row = this.getMirrorRow()
    if (row != null) return 100 * (row + 1)

    val col = this.getMirrorCol()
    if (col != null) return col + 1

    return null
  }

  fun List<String>.toGridList(): List<List<String>> {
    return this.joinToString(",").split(",,").map { it.split(",") }
  }

  fun part1(input: List<String>): Int {
    return input.toGridList().sumOf { it.getScore()!! }
  }

  fun List<String>.getScore2(excludedRow: Int = -1, excludedCol: Int = -1): Int? {
    val row = this.getMirrorRow(excludedRow)
    if (row != null) return 100 * (row + 1)

    val col = this.getMirrorCol(excludedCol)
    if (col != null) return col + 1

    return null
  }

  fun List<String>.getNewScore(): Int {
    val oldScore = this.getScore()!!
    val row = if(oldScore >= 100) oldScore / 100 - 1 else -1
    val col = if(oldScore >= 100) -1 else oldScore - 1

    val grid = this.map { it.toCharArray() }.toTypedArray()

    for (i in grid.indices) {
      for (j in grid[0].indices) {
        val old = grid[i][j]
        val new = if (old == '.') '#' else '.'

        grid[i][j] = new

        val newScore = grid.map { it.joinToString("") }.getScore2(row, col)
        if (newScore != null) return newScore

        grid[i][j] = old
      }
    }

    return 0
  }


  fun part2(input: List<String>): Int {
    return input.toGridList().sumOf { it.getNewScore() }
  }

  val testInput = readInput("Day13_test")
  check(part1(testInput) == 405)
  check(part2(testInput) == 400)

  val input = readInput("Day13")
  part1(input).println()
  part2(input).println()
}
