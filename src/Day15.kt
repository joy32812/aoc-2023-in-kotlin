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

  data class Lens(val label: String, val len: Int)
  fun part2(input: List<String>): Int {
    val boxes = Array(256) { mutableListOf<Lens>() }

    val operations = input.flatMap { it.split(",") }
    for (op in operations) {
      val (label, len) =
      if (op.last() == '-') {
        op.substring(0, op.length - 1) to 0
      } else {
        op.substring(0, op.length - 2) to op.last() - '0'
      }

      val id = label.toHashInt()

      if (len == 0) {
        boxes[id].removeIf { it.label == label }
      } else {
        val index = boxes[id].indexOfFirst { it.label == label }
        if (index != -1) {
          boxes[id][index] = Lens(label, len)
        } else {
          boxes[id] += Lens(label, len)
        }
      }
    }

    return boxes.indices.sumOf { i ->
      boxes[i].withIndex().sumOf { (i + 1) * (it.index + 1) * it.value.len }
    }
  }

  val testInput = readInput("Day15_test")
  check(part1(testInput) == 1320)
  check(part2(testInput) == 145)

  val input = readInput("Day15")
  part1(input).println()
  part2(input).println()
}
