fun main() {

  fun String.toArrangeCnt(): Long {
    val pattern = split(" ")[0]
    val result = split(" ")[1].split(",").map { it.toInt() }

    val ansMap = mutableMapOf<Int, Long>()

    fun dfs(i: Int, j: Int, cntP: Int, sumR: Int): Long {
      if (i >= pattern.length && j >= result.size) return 1
      if (i >= pattern.length || cntP < sumR) return 0

      val key = i * 100000 + j
      if (key in ansMap) return ansMap[key]!!

      if (pattern[i] == '.') {
        val ans = dfs(i + 1, j, cntP, sumR)
        ansMap[key] = ans
        return ans
      }

      if (pattern[i] == '#') {
        if (j >= result.size || i + result[j] > pattern.length || (0 until result[j]).any { pattern[i + it] == '.' }) {
          ansMap[key] = 0
          return 0
        }

        if (i + result[j] < pattern.length && pattern[i + result[j]] == '#') {
          ansMap[key] = 0
          return 0
        }

        val newCntP = if (i + result[j] < pattern.length && pattern[i + result[j]] == '?') {
          cntP - result[j] - 1
        } else {
          cntP - result[j]
        }

        val ans = dfs(i + result[j] + 1, j + 1, newCntP, sumR - result[j])
        ansMap[key] = ans
        return ans
      }

      var ans = dfs(i + 1, j, cntP - 1, sumR)

      if (j < result.size && i + result[j] <= pattern.length && (0 until result[j]).none { pattern[i + it] == '.' }) {

        if (i + result[j] >= pattern.length || pattern[i + result[j]] != '#') {
          val newCntP = if (i + result[j] < pattern.length && pattern[i + result[j]] == '?') {
            cntP - result[j] - 1
          } else {
            cntP - result[j]
          }
          ans += dfs(i + result[j] + 1, j + 1, newCntP, sumR - result[j])
        }
      }

      ansMap[key] = ans
      return ans
    }

    val cntX = pattern.count { it != '.' }
    val sum = result.sum()

    return dfs(0, 0, cntX, sum)
  }

  fun part1(input: List<String>): Long {
    return input.sumOf { it.toArrangeCnt() }
  }

  fun part2(input: List<String>): Long {

    return input.map {
      val (p, r) = it.split(" ")
      val newP = (0 until 5).joinToString("?") { p }
      val newR = (0 until 5).joinToString(",") { r }
      "$newP $newR"
    }.sumOf { it.toArrangeCnt() }
  }

  val testInput = readInput("Day12_test")
  check(part1(testInput) == 21L)
  check(part2(testInput) == 525152L)

  val input = readInput("Day12")
  part1(input).println()
  part2(input).println()
}
