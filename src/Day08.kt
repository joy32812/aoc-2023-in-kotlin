

fun main() {

  fun List<String>.toNetwork(): Map<String, Pair<String, String>> {
    return this.subList(2, this.size)
      .associate { s ->
        val id = s.split(" ")[0]
        val left = s.split(" ")[2].filter { it !in "()," }
        val right = s.split(" ")[3].filter { it !in "()," }

        id to Pair(left, right)
      }
  }

  fun part1(input: List<String>): Int {
    val instructions = input.first()
    val network = input.toNetwork()

    var ans = 0
    var cur = "AAA"
    var i = 0
    while (cur != "ZZZ") {

      cur = if (instructions[i] == 'L') {
        network[cur]!!.first
      } else {
        network[cur]!!.second
      }
      ans ++
      i = (i + 1) % instructions.length
    }

    return ans
  }

  fun List<String>.allEndsWithZ() = this.all { it.endsWith("Z") }

  data class Pattern(
    val circlePoints: Set<Int>,
    val circleLen: Int,
    val pre: Int
  )

  fun String.toPattern(instructions: String, network: Map<String, Pair<String, String>>): Pattern {
    val points = mutableListOf<Int>()
    val visited = mutableMapOf<Pair<String, Int>, Int>()
    var i = 0
    var len = 0
    var cur = this

    visited += (cur to i) to len

    while (true) {
      cur = if (instructions[i] == 'L') {
        network[cur]!!.first
      } else {
        network[cur]!!.second
      }
      i = (i + 1) % instructions.length
      len ++

      if (cur.endsWith("Z")) {
        points += len
      }

      if (cur to i in visited) {
        val pre = visited[cur to i]!!

        val circlePoints = points.filter { it >= pre }.map { it - pre }.toSet()
//        val circlePoints = points.subList(pre - 1, points.size).map { it - pre }.toSet()
        return Pattern(circlePoints, len - pre, pre)
      }
      visited += (cur to i) to len
    }
  }

  fun part2(input: List<String>): Long {
    val instructions = input.first()
    val network = input.toNetwork()

    val cur = network.keys.filter { it.endsWith("A") }
    val patterns = cur.map { it.toPattern(instructions, network) }.sortedByDescending { it.circleLen }
    val maxLen = patterns[0].circleLen

    var i = 0
    while (true) {
      for (p in patterns[0].circlePoints.sorted()) {
        val v = 1L * i * maxLen + p + patterns[0].pre

        val yes = patterns.all { pattern ->
          pattern.circlePoints.any { ((v - pattern.pre) % pattern.circleLen).toInt() == it }
        }
        if (yes) {
          return v
        }
      }
      i ++
    }
  }

//  val testInput = readInput("Day08_test")
//  check(part1(testInput) == 6)

  val testInput1 = readInput("Day08_test1")
  check(part2(testInput1) == 6L)

  val input = readInput("Day08")
  part1(input).println()
  part2(input).println()
}
