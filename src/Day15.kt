fun main() {

  fun String.toHashInt(): Int {
    var ans = 0

    for (c in this) {
      ans = (ans + c.code) * 17 % 256
    }
    return ans
  }

  fun part1(input: List<String>): Int {
    return input.flatMap { it.split(",") }.sumOf { it.toHashInt() }
  }

  fun part2(input: List<String>): Int {
    return 1
  }

  val testInput = readInput("Day15_test")
  check(part1(testInput) == 1320)
  check(part2(testInput) == 1)

  val input = readInput("Day15")
  part1(input).println()
  part2(input).println()
}
