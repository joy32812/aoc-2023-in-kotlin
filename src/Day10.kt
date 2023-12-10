fun main() {

  data class Index(
    val x: Int,
    val y: Int
  )

  fun List<String>.toStartIndex(): Index {
    for (i in this.indices) {
      for (j in this[i].indices) {
        if (this[i][j] == 'S') return Index(i, j)
      }
    }
    return Index(-1, -1)
  }

  fun Index.toNeighbours(grid: Array<CharArray>): List<Index> {
    val nexts = when (grid[this.x][this.y]) {
      '|' -> listOf(
        Index(this.x - 1, this.y),
        Index(this.x + 1, this.y)
      )
      '-' -> listOf(
        Index(this.x, this.y - 1),
        Index(this.x, this.y + 1)
      )
      'L' -> listOf(
        Index(this.x - 1, this.y),
        Index(this.x, this.y + 1)
      )
      'J' -> listOf(
        Index(this.x - 1, this.y),
        Index(this.x, this.y - 1)
      )
      '7' -> listOf(
        Index(this.x + 1, this.y),
        Index(this.x, this.y - 1)
      )
      'F' -> listOf(
        Index(this.x + 1, this.y),
        Index(this.x, this.y + 1)
      )
      else -> emptyList()
    }

    return nexts.filter { it.x in grid.indices && it.y in grid[it.x].indices && grid[it.x][it.y] != '.' }
  }

  fun List<String>.toCharArray2D(): Array<CharArray> {
    return this.map { it.toCharArray() }.toTypedArray()
  }

  fun List<String>.checkLoop(start: Char): Int {
    val startIndex = this.toStartIndex()
    val (sx, sy) = startIndex
    val grid = this.toCharArray2D()
    grid[sx][sy] = start

    val neighbours = startIndex.toNeighbours(grid)
    if (neighbours.size != 2) return 0

    grid[sx][sy] = '.'
    val (from, to) = neighbours
    val visited = mutableSetOf<Index>().apply { add(from) }
    val queue = mutableListOf(from)

    while (queue.isNotEmpty()) {
      val cur = queue.removeFirst()

      for (next in cur.toNeighbours(grid)) {
        if (next in visited) continue

        visited.add(next)
        queue.add(next)
      }
    }

    // No loop.
    if (to !in visited) return 0

    // Found loop.
    return (visited.size + 1) / 2
  }

  fun part1(input: List<String>): Int {
    return "|-LJ7F".map { input.checkLoop(it) }.max()
  }

  /*** Part 2 ***/

  // Split each tile into 4 small tiles.
  fun getNestCount(grid: Array<CharArray>, visited: MutableSet<Index>): Int {
    val n = grid.size
    val m = grid[0].size

    val mat = Array(n + n + 1) { IntArray(m + m + 1) }
    for ((x, y) in visited) {
      val ch = grid[x][y]

      val mx = x * 2 + 1
      val my = y * 2 + 1

      mat[mx][my] = 1

      when (ch) {
        '|' -> {
          mat[mx - 1][my] = 1
          mat[mx + 1][my] = 1
        }
        '-' -> {
          mat[mx][my - 1] = 1
          mat[mx][my + 1] = 1
        }
        'L' -> {
          mat[mx - 1][my] = 1
          mat[mx][my + 1] = 1
        }
        'J' -> {
          mat[mx - 1][my] = 1
          mat[mx][my - 1] = 1
        }
        '7' -> {
          mat[mx + 1][my] = 1
          mat[mx][my - 1] = 1
        }
        'F' -> {
          mat[mx + 1][my] = 1
          mat[mx][my + 1] = 1
        }
      }
    }

    val sx = 0
    val sy = 0

    val dx = listOf(0, 0, 1, -1)
    val dy = listOf(1, -1, 0, 0)

    val Q = mutableListOf<Index>().apply { add(Index(sx, sy)) }
    mat[sx][sy] = -1

    while (Q.isNotEmpty()) {
      val cur = Q.removeFirst()

        for (i in dx.indices) {
            val nx = cur.x + dx[i]
            val ny = cur.y + dy[i]

            if (nx !in mat.indices || ny !in mat[nx].indices) continue
            if (mat[nx][ny] != 0) continue

            mat[nx][ny] = -1
            Q.add(Index(nx, ny))
        }
    }

    var ans = 0
    for (i in mat.indices) {
        for (j in mat[i].indices) {
          if (i % 2 == 1 && j % 2 == 1 && mat[i][j] == 0) {
            ans ++
          }
        }
    }

    return ans
  }

  fun List<String>.findNests(start: Char): Int {
    val startIndex = this.toStartIndex()
    val (sx, sy) = startIndex
    val grid = this.toCharArray2D()
    grid[sx][sy] = start

    val neighbours = startIndex.toNeighbours(grid)
    if (neighbours.size != 2) return 0

    grid[sx][sy] = '.'
    val (from, to) = neighbours
    val visited = mutableSetOf<Index>().apply { add(from) }
    val queue = mutableListOf(from)

    while (queue.isNotEmpty()) {
      val cur = queue.removeFirst()

      for (next in cur.toNeighbours(grid)) {
        if (next in visited) continue

        visited.add(next)
        queue.add(next)
      }
    }

    // No loop.
    if (to !in visited) return 0

    // Found loop.
    visited += startIndex

    grid[sx][sy] = start
    return getNestCount(grid, visited)
  }

  fun part2(input: List<String>): Int {
//    val tmp = "|-LJ7F".map { input.findNests(it) }
//    println(tmp)

    return "|-LJ7F".map { input.findNests(it) }.max()
  }

  val testInput = readInput("Day10_test")
  check(part1(testInput) == 8)

  val testInput1 = readInput("Day10_test1")
  check(part2(testInput1) == 8)

  val input = readInput("Day10")
  part1(input).println()
  part2(input).println()
}
