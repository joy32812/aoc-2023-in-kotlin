fun main() {

  data class CardHand(
    val hand: String,
    val bid: Int
  )

  fun String.toType(): Int {
    val gp = this.groupBy { it }.mapValues { it.value.size }
    val cntStr = gp.values.sortedDescending().joinToString("_")

    return when (cntStr) {
      "5" -> 7
      "4_1" -> 6
      "3_2" -> 5
      "3_1_1" -> 4
      "2_2_1" -> 3
      "2_1_1_1" -> 2
      "1_1_1_1_1" -> 1
      else -> 0
    }
  }

  fun CardHand.toType() = this.hand.toType()

  fun CardHand.toOrderHand(): String {
    return this.hand.map { c ->
      when (c) {
        'T' -> '9' + 1
        'J' -> '9' + 2
        'Q' -> '9' + 3
        'K' -> '9' + 4
        'A' -> '9' + 5
        else -> c
      }
    }.joinToString("")
  }

  fun String.toCardHand(): CardHand {
    val splits = this.split(" ")
    return CardHand(splits[0], splits[1].toInt())
  }

  fun List<String>.toCardHands(): List<CardHand> {
    return this.map { it.toCardHand() }
  }

  fun part1(input: List<String>): Long {
    val hands = input.toCardHands().sortedWith { a, b ->
      if (a.toType() == b.toType()) a.toOrderHand().compareTo(b.toOrderHand())
      else a.toType() - b.toType()
    }
    return hands.withIndex().sumOf { (i, v) -> 1L * (i + 1) * v.bid }
  }

  /*=================Part2===============*/

  fun CardHand.toNewOrderHand(): String {
    return this.hand.map { c ->
      when (c) {
        'T' -> '9' + 1
        'J' -> '1'
        'Q' -> '9' + 3
        'K' -> '9' + 4
        'A' -> '9' + 5
        else -> c
      }
    }.joinToString("")
  }

  fun CardHand.toBestType(): Int {
    val possibleChars = "23456789TQKA"

    return possibleChars.map { c ->
      hand.map { if (it == 'J') c else it }.joinToString("").toType()
    }.max()
  }

  fun part2(input: List<String>): Long {
    val hands = input.toCardHands().sortedWith { a, b ->
      if (a.toBestType() == b.toBestType()) a.toNewOrderHand().compareTo(b.toNewOrderHand())
      else a.toBestType() - b.toBestType()
    }
    return hands.withIndex().sumOf { (i, v) -> 1L * (i + 1) * v.bid }  }

  val testInput = readInput("Day07_test")
  check(part1(testInput) == 6440L)
  check(part2(testInput) == 5905L)

  val input = readInput("Day07")
  part1(input).println()
  part2(input).println()
}