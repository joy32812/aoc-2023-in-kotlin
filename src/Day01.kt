fun main() {
    fun part1(input: List<String>): Int {
        return input.map { s -> s.filter { it.isDigit() } }
            .sumOf { "${it.first()}${it.last()}".toInt() }
    }

    fun part2(input: List<String>): Int {

        val str2Int = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )

        fun String.firstDigit(reversed: Boolean = false): Int {
            val indices = if (reversed) this.indices.reversed() else this.indices

            for (i in indices) {
                if (this[i].isDigit()) return "${this[i]}".toInt()

                for ((k, v) in str2Int) {
                    if (this.startsWith(k, i)) return v
                }
            }
            return 0
        }

        return input.sumOf { it.firstDigit() * 10 + it.firstDigit(true) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
