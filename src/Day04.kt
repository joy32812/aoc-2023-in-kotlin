fun main() {

  data class Card(
    val winningNumbers: List<Int>,
    val myNumbers: List<Int>
  )

  fun String.toIntList(): List<Int> {
    return this.split(" ").mapNotNull { if (it.isEmpty()) null else it.toInt() }
  }

  fun String.toCard(): Card {
    val numStr = this.split(": ")[1]
    val splits = numStr.split(" | ")

    return Card(splits[0].toIntList(), splits[1].toIntList())
  }

  fun Card.toScore(): Long {
    val cnt = myNumbers.count { it in winningNumbers }
    return 1L shl (cnt - 1)
  }

  fun part1(input: List<String>): Long {
    return input.sumOf { it.toCard().toScore() }
  }

  fun part2(input: List<String>): Long {
    val n = input.size
    val counts = Array(n) { 1L }

    val cards = input.map { it.toCard() }
    for (i in cards.indices) {
      val matchCnt = cards[i].myNumbers.count { it in cards[i].winningNumbers }

      for (j in i + 1 .. i + matchCnt) {
        if (j >= n) break
        counts[j] += counts[i]
      }
    }

    return counts.sum()
  }

  val testInput = readInput("Day04_test")
  check(part1(testInput) == 13L)
  check(part2(testInput) == 30L)

  val input = readInput("Day04")
  part1(input).println()
  part2(input).println()

}