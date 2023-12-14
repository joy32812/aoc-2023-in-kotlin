fun main() {

  fun Array<CharArray>.toScore(): Int {
    var ans = 0
    for (i in this.indices) {
      for (j in this[i].indices) {
        if (this[i][j] == 'O') ans += this.size - i
      }
    }
    return ans
  }

  fun Array<CharArray>.toNorthTilted(): Array<CharArray> {
    for (i in this.indices) {

      for (j in this[i].indices) {
        if (this[i][j] != 'O') continue

        var k = i
        while (k - 1 >= 0 && this[k - 1][j] == '.') {
          this[k][j] = '.'
          this[k - 1][j] = 'O'
          k --
        }
      }
    }

    return this
  }

  fun Array<CharArray>.toSouthTilted(): Array<CharArray> {
    val n = this.size
    for (i in this.indices.reversed()) {
      for (j in this[i].indices) {
        if (this[i][j] != 'O') continue

        var k = i
        while (k + 1 < n && this[k + 1][j] == '.') {
          this[k][j] = '.'
          this[k + 1][j] = 'O'
          k ++
        }
      }
    }

    return this
  }
  fun Array<CharArray>.toWestTilted(): Array<CharArray> {
    for (i in this.indices) {
      for (j in this[i].indices) {
        if (this[i][j] != 'O') continue

        var k = j
        while (k - 1 >= 0 && this[i][k - 1] == '.') {
          this[i][k] = '.'
          this[i][k - 1] = 'O'
          k --
        }
      }
    }

    return this
  }

  fun Array<CharArray>.toEastTilted(): Array<CharArray> {
    val m = this[0].size

    for (i in this.indices) {
      for (j in this[i].indices.reversed()) {
        if (this[i][j] != 'O') continue

        var k = j
        while (k + 1 < m && this[i][k + 1] == '.') {
          this[i][k] = '.'
          this[i][k + 1] = 'O'
          k ++
        }
      }
    }

    return this
  }

  fun part1(input: List<String>): Int {
    val grid = input.map { it.toCharArray() }.toTypedArray()
    return grid.toNorthTilted().toScore()
  }

  fun Array<CharArray>.toIdentifier(): String {
    return this.indices.flatMap { i -> this[i].indices.map { j -> i to j } }
      .filter { (i, j) -> this[i][j] == 'O' }
      .joinToString(",") { (i, j) -> "${i}_${j}" }
  }

  // Find cycle length and the pre length before the cycle.
  fun part2(input: List<String>): Int {
    val grid = input.map { it.toCharArray() }.toTypedArray()

    val cntMap = mutableMapOf<Int, String>()
    val idMap = mutableMapOf<String, Int>()
    idMap[grid.toIdentifier()] = 0
    cntMap[0] = grid.toIdentifier()

    var cnt = 1

    var cycle = 0
    var pre = 0
    var now = grid
    while (true) {
      now = now.toNorthTilted().toWestTilted().toSouthTilted().toEastTilted()
      val id = now.toIdentifier()

      if (id in idMap) {
        pre = idMap[id]!!
        cycle = cnt - pre
        break
      }

      idMap[id] = cnt
      cntMap[cnt] = id

      cnt ++
    }

    val times = 1000000000
    val id = cntMap[(times - pre - 1) % cycle + pre + 1]!!

    return id.split(",").map { grid.size - it.split("_")[0].toInt() }.sum()
  }

  val testInput = readInput("Day14_test")
  check(part1(testInput) == 136)
  check(part2(testInput) == 64)

  val input = readInput("Day14")
  part1(input).println()
  part2(input).println()
}