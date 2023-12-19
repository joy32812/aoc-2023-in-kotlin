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



  data class RangeRating(
    val x: IntRange,
    val m: IntRange,
    val a: IntRange,
    val s: IntRange,
  )

  fun List<Workflow>.toAllRangeRatings(): List<RangeRating> {
    val (xRanges, mRanges, aRanges, sRanges) =  "xmas".map { ch ->
      val points = this.flatMap { w ->
        w.rules.mapNotNull { rule ->
          if (rule.length < 2 || rule[1] !in "<>" || rule[0] != ch) null
          else rule.substring(2).split(":")[0].toInt()
        }
      }
        .toMutableList()
        .apply {
          add(1)
          add(4000)
        }
        .toSet()
        .sorted()

      points
        .zipWithNext()
        .flatMap {
          val list = mutableListOf(it.first .. it.first)
          if (it.first + 1 <= it.second - 1) {
            list += it.first + 1..<it.second
          }
          list
        }
        .toMutableList()
        .apply { add(points.last() .. points.last()) }
    }

    val result = mutableListOf<RangeRating>()

    for (x in xRanges) {
      for (m in mRanges) {
        for (a in aRanges) {
          for (s in sRanges) {
            result += RangeRating(x, m, a, s)
          }
        }
      }
    }
    return result
  }

  fun RangeRating.toResultForRule(rule: String): String? {
    if (rule.length < 2) return rule
    if (rule[1] !in "<>") return rule

    val ch = rule[0]
    val v = rule.substring(2).split(":")[0].toInt()
    val result = rule.substring(2).split(":")[1]

    val range = when (ch) {
      'x' -> x
      'm' -> m
      'a' -> a
      else -> s
    }

    if (rule[1] == '<' && range.last < v) return result
    if (rule[1] == '>' && range.first > v) return result

    return null
  }

  fun RangeRating.canBeAccepted(workflowMap: Map<String, Workflow>): Boolean {
    var now = "in"
    while (true) {
      val workflow = workflowMap[now]!!

      for (rule in workflow.rules) {

        val result = this.toResultForRule(rule)
        if (result != null) {
          now = result
          break
        }
      }

      if (now == "A") return true
      if (now == "R") return false
    }
  }

  fun part2(input: List<String>): Long {
    val workflows = input.toWorkflows()
    val workflowMap = workflows.associateBy { it.id }

    val rangeRatings = workflows.toAllRangeRatings()

    fun IntRange.len(): Long {
      return this.last - this.first + 1L
    }
    fun IntRange.sum(): Long {
      return 1L * (first + last) * len() / 2
    }

    val filter = rangeRatings.filter { it.canBeAccepted(workflowMap) }
    val ans = filter.sumOf { r ->
      r.x.len() * r.m.len() * r.a.len() * r.s.len()
    }
    return ans
  }

  val testInput = readInput("Day19_test")
  check(part1(testInput) == 19114)
  // check(part2(testInput) == 167409079868000)

  val input = readInput("Day19")
  // part1(input).println()
  part2(input).println()
}