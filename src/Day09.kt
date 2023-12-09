fun main() {

  fun List<Int>.getDiff(): List<Int> {
    return this.zipWithNext().map { it.second - it.first }
  }

  fun List<Int>.getAns(): Int {
    var tmp = this
    var ans = 0

    while (tmp.size > 1) {
      ans += tmp.last()
      tmp = tmp.getDiff()
    }

    return ans + tmp.first()
  }

  fun String.toIntList(): List<Int> {
    return this.split(" ").map { it.toInt() }
  }

  fun part1(input: List<String>): Int {
    return input.sumOf { it.toIntList().getAns() }
  }

  fun List<Int>.getAns2(): Int {
    val tmps = mutableListOf<List<Int>>()
    var tmp = this

    while (tmp.size > 1) {
      tmps.add(tmp)
      tmp = tmp.getDiff()
    }

    var now = tmp.first()
    for (i in tmps.size - 1 downTo 0) {
      now = tmps[i].first() - now
    }

    return now
  }

  fun part2(input: List<String>): Int {
    return input.sumOf { it.toIntList().getAns2() }
  }

  val testInput = readInput("Day09_test")
  check(part1(testInput) == 114)
  check(part2(testInput) == 2)

  val input = readInput("Day09")
  part1(input).println()
  part2(input).println()
}
