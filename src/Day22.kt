import java.util.*

fun main() {

  data class Index(
    val x: Int,
    val y: Int,
    val z: Int
  )

  data class Brick(
    var cubes : Set<Index>,
    var id: Int = 0,
  )

  fun String.toBrick(): Brick {
    val splits = split("~")
    val (x1, y1, z1) = splits[0].split(",").map { it.toInt() }
    val (x2, y2, z2) = splits[1].split(",").map { it.toInt() }
    val cubes = mutableSetOf<Index>()
    for (x in x1..x2) {
      for (y in y1..y2) {
        for (z in z1..z2) {
          cubes.add(Index(x, y, z))
        }
      }
    }

    return Brick(cubes)
  }

  fun List<String>.toBricks() = map { it.toBrick() }

  fun Brick.canDrop(otherCubeMap: Map<Index, Int>): Boolean {
    val minZ = cubes.minOf { it.z }
    if (minZ == 1) return false

    val newCubes = cubes.map { Index(it.x, it.y, it.z - 1) }.toSet()
    if (newCubes.none { it in otherCubeMap }) return true

    return false
  }

  fun Brick.drop() {
    val newCubes = cubes.map { Index(it.x, it.y, it.z - 1) }.toSet()
    cubes = newCubes
  }

  fun List<String>.toStableBricks(): List<Brick> {
    val bricks = this.toBricks().sortedBy { b -> b.cubes.minOf { it.z } }
    bricks.forEachIndexed { index, brick ->
      brick.id = index
    }

    var index2Id = bricks.flatMap { brick -> brick.cubes.map { it to brick.id } }.toMap()
    for (brick in bricks) {
      val otherCubeMap = index2Id.filter { it.value != brick.id }

      while (brick.canDrop(otherCubeMap)) {
        brick.drop()
      }

      index2Id = index2Id.filter { it.value != brick.id } + brick.cubes.map { it to brick.id }
    }

    return bricks
  }

  fun part1(input: List<String>): Int {
    val bricks = input.toStableBricks()
    val index2Id = bricks.flatMap { brick -> brick.cubes.map { it to brick.id } }.toMap()

    val canRemove = BooleanArray(bricks.size) { true }
    for (brick in bricks) {
      val newCubes = brick.cubes.map { Index(it.x, it.y, it.z - 1) }.toSet()
      val otherCubeMap = index2Id.filter { it.value != brick.id }

      val otherBrickIds = newCubes.mapNotNull { otherCubeMap[it] }.toSet()
      if (otherBrickIds.size == 1) {
        canRemove[otherBrickIds.first()] = false
      }
    }

    return canRemove.count { it }
  }

  fun List<Brick>.dropCountIfRemove(removedBrick: Brick): Int {

    val supportedByMap = mutableMapOf<Int, MutableSet<Int>>()
    val supportingMap = mutableMapOf<Int, MutableSet<Int>>()

    val index2Id = this.flatMap { brick -> brick.cubes.map { it to brick.id } }.toMap()
    for (brick in this) {
      val newCubes = brick.cubes.map { Index(it.x, it.y, it.z - 1) }.toSet()
      val otherCubeMap = index2Id.filter { it.value != brick.id }

      val otherBrickIds = newCubes.mapNotNull { otherCubeMap[it] }.toSet()

      supportedByMap[brick.id] = otherBrickIds.toMutableSet()
      for (otherBrickId in otherBrickIds) {
        supportingMap.getOrPut(otherBrickId) { mutableSetOf() } += brick.id
      }
    }


    val dropIdSet = mutableSetOf(removedBrick.id)
    val Q = LinkedList<Brick>().apply { add(removedBrick) }

    while (Q.isNotEmpty()) {
      val cur = Q.poll()

      val supportingBrickIds = supportingMap[cur.id] ?: continue

      for (id in supportingBrickIds) {
        val supportedBrickIds = supportedByMap[id] ?: continue

        if (supportedBrickIds.all { it in dropIdSet }) {
          dropIdSet += id
          Q += this[id]
        }
      }
    }

    return dropIdSet.size - 1
  }

  fun part2(input: List<String>): Int {
    val bricks = input.toStableBricks()
    return bricks.sumOf { brick -> bricks.dropCountIfRemove(brick) }
  }

  val testInput = readInput("Day22_test")
  check(part1(testInput) == 5)
  check(part2(testInput) == 7)

  val input = readInput("Day22")
  part1(input).println()
  part2(input).println()
}
