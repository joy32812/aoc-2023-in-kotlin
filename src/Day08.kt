

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

  fun part2(input: List<String>): Long {
    val instructions = input.first()
    val network = input.toNetwork()

    var cur = network.keys.filter { it.endsWith("A") }
    var ans = 0L
    var i = 0

    while (!cur.allEndsWithZ()) {
      cur = if (instructions[i] == 'L') {
        cur.map { network[it]!!.first }
      } else {
        cur.map { network[it]!!.second }
      }
      ans ++
      i = (i + 1) % instructions.length
    }

    return ans
  }

  val testInput = readInput("Day08_test")
  check(part1(testInput) == 6)

  val testInput1 = readInput("Day08_test1")
  check(part2(testInput1) == 6L)

  val input = readInput("Day08")
  part1(input).println()
  part2(input).println()
}