fun main() {

  data class Record(val time: Int, val dis: Int)

  fun List<String>.toRecords(): List<Record> {
    val times = this[0].split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val dises = this[1].split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }

    return times.zip(dises).map { Record(it.first, it.second) }
  }

  fun part1(input: List<String>): Int {
    val records = input.toRecords()

    val results = records.map { (time, dis) ->
      (1..time).count { t -> t * (time - t) > dis }
    }

    return results.fold(1) { acc, d -> acc * d }
  }

  // Binary search to find the first OK in the left half.
  fun firstOK(left: Int, right: Int, time: Int, dis: Long): Int {
    var l = left
    var r = right

    while (l < r) {
      val m = (l + r) / 2
      val newDis = 1L * m * (time - m)

      if (newDis > dis) r = m
      else l = m + 1
    }

    return l
  }

  // Binary search to find the first Not OK in the right half.
  fun firstNotOK(left: Int, right: Int, time: Int, dis: Long): Int {
    var l = left
    var r = right

    while (l < r) {
      val m = (l + r) / 2
      val newDis = 1L * m * (time - m)

      if (newDis <= dis) r = m
      else l = m + 1
    }

    return l
  }

  fun part2(input: List<String>): Int {
    val time = input[0].filter { it.isDigit() }.toInt()
    val dis = input[1].filter { it.isDigit() }.toLong()

    val max = 1L * (time / 2) * (time - time / 2)
    if (max <= dis) return 0

    val l = firstOK(0, time / 2, time, dis)
    val r = firstNotOK(time / 2, time, time, dis)

    return r - l
  }

  val testInput = readInput("Day06_test")
  check(part1(testInput) == 288)
  check(part2(testInput) == 71503)

  val input = readInput("Day06")
  part1(input).println()
  part2(input).println()


}