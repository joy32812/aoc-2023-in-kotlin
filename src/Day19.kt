fun main() {

  data class Workflow(
    val id: String,
    val rules: List<String>
  )

  fun List<String>.toWorkflows(): List<Workflow> {
    fun String.toWorkflow(): Workflow {
      val splits = this.split("{")
      val id = splits[0]
      val rules = splits[1].filter { it != '}' }.split(",")
      return Workflow(id, rules)
    }

    val workflowStringList = this.joinToString("_").split("__")[0].split("_")
    return workflowStringList.map { it.toWorkflow() }
  }

  fun List<String>.toRatings(): List<Map<Char, Int>> {
    fun String.toRating(): Map<Char, Int> {
      return this.filter { it !in "{}" }.split(",").associate {
        val ch = it[0]
        val v = it.substring(2).toInt()
        ch to v
      }
    }

    val ratingStringList = this.joinToString("_").split("__")[1].split("_")
    return ratingStringList.map { it.toRating() }
  }

  fun Map<Char, Int>.toResultForRule(rule: String): String? {
    if (rule.length < 2) return rule
    if (rule[1] !in "<>") return rule

    val ch = rule[0]
    val v = rule.substring(2).split(":")[0].toInt()
    val result = rule.substring(2).split(":")[1]

    if (rule[1] == '<' && this[ch]!! < v) return result
    if (rule[1] == '>' && this[ch]!! > v) return result
    return null
  }

  fun Map<Char, Int>.canBeAccepted(workflowMap: Map<String, Workflow>): Boolean {
    var now = "in"

    while (true) {
      val workflow = workflowMap[now]!!

      for (rule in workflow.rules) {

        val result = this.toResultForRule(rule)
        if (result != null){
          now = result
          break
        }
      }

      if (now == "A") return true
      if (now == "R") return false
    }
  }

  fun part1(input: List<String>): Int {
    val workflowMap = input.toWorkflows().associateBy { it.id }
    val ratings = input.toRatings()

    return ratings.filter { it.canBeAccepted(workflowMap) }.sumOf { it.values.sum() }
  }


  val char2Index = mutableMapOf(
    'x' to 0,
    'm' to 1,
    'a' to 2,
    's' to 3,
  )
  fun part2(input: List<String>): Long {
    val workflows = input.toWorkflows()
    val workflowMap = workflows.associateBy { it.id }

    var ans = 0L

    fun dfs(now: String, ranges: List<IntRange>) {
      if (now == "R") return
      if (now == "A") {
        ans += ranges.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
        return
      }

      val workflow = workflowMap[now]!!
      val curRange = ranges.toMutableList()

      for (rule in workflow.rules) {
        if (rule.length < 2 || rule[1] !in "<>") {
          dfs(rule, curRange)
          break
        }

        val ch = rule[0]
        val v = rule.substring(2).split(":")[0].toInt()
        val result = rule.substring(2).split(":")[1]

        val index = char2Index[ch]!!

        if (rule[1] == '<') {
          if (curRange[index].first < v) {
            val newRange = curRange.toMutableList()
            newRange[index] = curRange[index].first..(v - 1)
            dfs(result, newRange)

            curRange[index] = v..curRange[index].last
          }
        } else {
          if (curRange[index].last > v) {
            val newRange = curRange.toMutableList()
            newRange[index] = (v + 1)..curRange[index].last
            dfs(result, newRange)

            curRange[index] = curRange[index].first..v
          }
        }
      }
    }

    dfs("in", Array(4) { 1..4000 }.toList())
    return ans
  }

  val testInput = readInput("Day19_test")
  check(part1(testInput) == 19114)
  check(part2(testInput) == 167409079868000L)

  val input = readInput("Day19")
  part1(input).println()
  part2(input).println()
}
