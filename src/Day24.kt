fun main() {

  data class HailStone(
    val x: Long,
    val y: Long,
    val z: Long,
    val dx: Long,
    val dy: Long,
    val dz: Long
  )

  fun String.toHailStone(): HailStone {
    val (x, y, z) = split(" @ ")[0].split(", ").map { it.trim().toLong() }
    val (dx, dy, dz) = split(" @ ")[1].split(", ").map { it.trim().toLong() }
    return HailStone(x, y, z, dx, dy, dz)
  }

  fun pathIntersectInRangeIgnoringZ(h1: HailStone, h2: HailStone, range: LongRange): Boolean {
    val t1 = 1.0 * h1.dy / h1.dx
    val c1 = h1.y - t1 * h1.x

    val t2 = 1.0 * h2.dy / h2.dx
    val c2 = h2.y - t2 * h2.x

    if (t1 == t2) return false

    val intersectX = (c2 - c1) / (t1 - t2)
    val intersectY = t1 * intersectX + c1

    if ((intersectX - h1.x) * h1.dx < 0) return false
    if ((intersectX - h2.x) * h2.dx < 0) return false
    if ((intersectY - h1.y) * h1.dy < 0) return false
    if ((intersectY - h2.y) * h2.dy < 0) return false

    return (intersectX >= range.first && intersectX <= range.last) &&
      (intersectY >= range.first && intersectY <= range.last)
  }

  fun part1(input: List<String>, range: LongRange): Int {
    val hailStones = input.map { it.toHailStone() }

    var ans = 0
    for (i in hailStones.indices) {
      for (j in i + 1 until hailStones.size) {
        if (pathIntersectInRangeIgnoringZ(hailStones[i], hailStones[j], range)) {
          ans++
        }
      }
    }

    return ans
  }

  fun part2(input: List<String>): Long {
    return 1
  }

  val testInput = readInput("Day24_test")
  check(part1(testInput, 7L..27L) == 2)
  check(part2(testInput) == 47L)

  val input = readInput("Day24")
  part1(input, 200000000000000L .. 400000000000000L).println()
//  part2(input).println()
}
