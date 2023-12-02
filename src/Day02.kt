

private data class Round(
    val red: Int = 0,
    val blue: Int = 0,
    val green: Int = 0,
)
private data class Game(
    val gameId: Int,
    val rounds: List<Round>,
)
fun main() {

    fun String.toRound(): Round {
        val balls = this.split(", ").map { it.split(" ") }
        var red = 0
        var blue = 0
        var green = 0
        for ((cnt, color) in balls) {
            when (color) {
                "red" -> red = cnt.toInt()
                "blue" -> blue = cnt.toInt()
                "green" -> green = cnt.toInt()
            }
        }
        return Round(red, blue, green)
    }
    fun String.toGame(): Game {
        val gameId = this.substringAfter("Game ").substringBefore(":").toInt()
        val rounds = this.substringAfter(": ").split("; ").map { it.toRound() }
        return Game(gameId, rounds)
    }

    fun part1(input: List<String>): Int {

        return input
            .map { it.toGame() }
            .filter { it.rounds.all { r -> r.red <= 12 && r.green <= 13 && r.blue <= 14 } }
            .sumOf { it.gameId }
    }

    fun part2(input: List<String>): Int {

        return input
            .map { it.toGame() }
            .sumOf {
                val red = it.rounds.maxOf { it.red }
                val blue = it.rounds.maxOf { it.blue }
                val green = it.rounds.maxOf { it.green }
                red * blue * green
            }
    }


    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
