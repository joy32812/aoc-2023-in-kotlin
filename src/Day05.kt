fun main() {

  data class OneMapping(
    val to: Long,
    val from: Long,
    val size: Long
  )

  data class GardenMap(
    val maps: List<OneMapping>
  )

  fun String.toOneMapping(): OneMapping {
    val nums = this.split(" ").map { it.toLong() }
    return OneMapping(nums[0], nums[1], nums[2])
  }

  fun OneMapping.getMappingValue(x: Long): Long? {
    if (x in from until from + size) return to + x - from
    return null
  }

  fun List<String>.toGardenMap(): GardenMap {
    val maps = this.map { it.toOneMapping() }
    return GardenMap(maps = maps)
  }

  fun GardenMap.getMapValue(x: Long): Long {
    for (map in this.maps) {
      val result = map.getMappingValue(x)
      if (result != null) return result
    }
    return x
  }


  fun List<String>.toSeeds(): List<Long> {
    return this[0].split(": ")[1].split(" ").map { it.toLong() }
  }

  fun List<String>.toGardenMapList(): List<GardenMap> {
    return this
      .subList(3, this.size)
      .map { if ("map" in it) "map" else it }
      .filter { it.isNotEmpty() }
      .joinToString(",")
      .split(",map,")
      .map { it.split(",").toGardenMap() }
  }

  fun List<GardenMap>.getMapValue(x: Long): Long {
    var cur = x
    for (gm in this) {
      cur = gm.getMapValue(cur)
    }

    return cur
  }

  fun part1(input: List<String>): Long {
    val seeds = input.toSeeds()
    val gardenMaps = input.toGardenMapList()

    return seeds.map { gardenMaps.getMapValue(it) }.min()
  }

  /*============Part2================*/
  fun List<String>.toRanges(): List<LongRange> {
    val nums = this[0].split(": ")[1].split(" ").map { it.toLong() }
    return nums.chunked(2).map { it[0] until it[0] + it[1] }
  }

  fun OneMapping.getReversedMappingValue(x: Long): Long? {
    if (x in to until to + size) return from + x - to
    return null
  }

  fun GardenMap.getReversedMappingValue(x: Long): Long {
    for (map in this.maps) {
      val result = map.getReversedMappingValue(x)
      if (result != null) return result
    }
    return x
  }

  fun List<GardenMap>.toPossiblePoints(): List<Long> {
    var points = emptyList<Long>()

    for (gm in this.reversed()) {
      val tmp = points.map { gm.getReversedMappingValue(it) }.toMutableList()
      tmp += gm.maps.flatMap { listOf(it.from, it.from + it.size - 1, it.to, it.to + it.size - 1) }

      points = tmp
    }

    return points
  }


  fun part2(input: List<String>): Long {
    val gardenMaps = input.toGardenMapList()

    val ranges = input.toRanges()
    val rangesPoints = ranges.flatMap { listOf(gardenMaps.getMapValue(it.first), gardenMaps.getMapValue(it.last)) }
    val possiblePoints = gardenMaps.toPossiblePoints().distinct().sorted()

    val allPoints = rangesPoints + possiblePoints

    return minOf(
      allPoints.mapNotNull { p ->
        if (ranges.none { r -> p in r }) null
        else gardenMaps.getMapValue(p)
      }.min()
    )
  }

  val testInput = readInput("Day05_test")
  check(part1(testInput) == 35L)
  check(part2(testInput) == 46L)

  val input = readInput("Day05")
  part1(input).println()
  part2(input).println()


}