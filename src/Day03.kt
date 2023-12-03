fun main() {
    data class EngineNumber(
        val num: Int,
        val row: Int,
        val col: Int,
    )

    fun EngineNumber.isAdjacentToASymbol(input: List<String>): Boolean {
        val i = this.row
        val from = this.col
        val to = this.col + this.num.toString().length - 1

        for (x in i - 1 .. i + 1) {
            for (y in from - 1 .. to + 1) {
                if (x !in input.indices) continue
                if (y !in input[x].indices) continue

                val ch = input[x][y]
                if (!ch.isDigit() && ch != '.') return true
            }
        }

        return false
    }

    fun List<String>.findAllNumbers(): List<EngineNumber> {
        val ans = mutableListOf<EngineNumber>()
        for (i in this.indices) {
            var num = 0
            var from = -1

            for (j in this[i].indices) {
                if (this[i][j].isDigit()) {
                    if (from == -1) from = j
                    num = num * 10 + "${this[i][j]}".toInt()
                } else {
                    if (from != -1) {
                        ans.add(EngineNumber(num, i, from))
                        num = 0
                        from = -1
                    }
                }
            }

            if (from != -1) {
                ans.add(EngineNumber(num, i, from))
            }
        }
        return ans
    }

    fun part1(input: List<String>): Int {
        return input.findAllNumbers().filter { it.isAdjacentToASymbol(input) }.sumOf { it.num }
    }

    fun EngineNumber.isAdjacentTo(x: Int, y: Int): Boolean {
        if (x !in this.row - 1 .. this.row + 1) return false
        val from = this.col
        val to = this.col + this.num.toString().length - 1

        return y in from - 1 .. to + 1
    }

    fun part2(input: List<String>): Int {
        val numbers = input.findAllNumbers()
        var ans = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] != '*') continue

                val filters = numbers.filter { it.isAdjacentTo(i, j) }
                if (filters.size == 2) ans += filters[0].num * filters[1].num
            }
        }

        return ans
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
