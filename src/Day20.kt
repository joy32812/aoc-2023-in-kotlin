import java.util.LinkedList

fun main() {

  data class Module(
    val id: String,
    val dests: List<String>,
    val type: Int, // 0: Flip-flop, 1: Conjunction, 2: passthrough
    var inputs: List<String> = emptyList(),
  )

  fun String.toModule(): Module {
    val (idWithType, dests) = split(" -> ")
    val (id, type) = when {
      idWithType.startsWith("%") -> idWithType.substring(1) to 0
      idWithType.startsWith("&") -> idWithType.substring(1) to 1
      else -> idWithType to 2
    }

    return Module(id, dests.split(", "), type)
  }

  fun List<String>.toModules(): List<Module> {
    val modules = map { it.toModule() }

    for (m in modules) {
      m.inputs = modules.filter { m.id in it.dests }.map { it.id }
    }
    return modules
  }

  data class State(
    val flipState: Map<String, Int>,
    val conjState: Map<String, Map<String, Int>>
  )
  data class Pulse(
    val id: String,
    val type: Int, // 0: low, 1: high
    val from: String,
  )

  fun Pulse.isLow() = type == 0

  fun List<Module>.toInitState(): State {
    val flipState = mutableMapOf<String, Int>()
    val conjState = mutableMapOf<String, Map<String, Int>>()
    for (m in this) {
      when (m.type) {
        0 -> flipState[m.id] = 0
        1 -> conjState[m.id] = m.inputs.associateWith { 0 }
      }
    }
    return State(flipState, conjState)
  }

  var idGotType = false
  val needId = "jq"
  val needPulseType = 1

  fun List<Module>.run(initState: State): Pair<LongArray, State> {
    val result = LongArray(2).apply { this[0] = 1 }
    val id2Module = associateBy { it.id }

    var curState = initState

    val Q = LinkedList<Pulse>().apply { add(Pulse("broadcaster", 0, "button")) }
    while (Q.isNotEmpty()) {
      val pulse = Q.poll()
      val module = id2Module[pulse.id] ?: continue

      val flipState = curState.flipState.toMutableMap()
      val conjState = curState.conjState.mapValues { it.value.toMutableMap() }.toMutableMap()

      var nextPulseType = 0
      var shouldPass = false
      when (module.type) {
        0 -> {
          if (pulse.isLow()) {
            nextPulseType = if (flipState[module.id]!! == 0) 1 else 0

            // switch
            flipState[module.id] = 1 - flipState[module.id]!!
            shouldPass = true
          }
        }
        1 -> {
          shouldPass = true
          conjState[module.id]!![pulse.from] = pulse.type
          nextPulseType = if (conjState[module.id]!!.values.all { it == 1 }) 0 else 1
        }
        else -> {
          shouldPass = true
          nextPulseType = pulse.type
        }
      }

      if (shouldPass) {
        for (dest in module.dests) {
          if (dest == needId && nextPulseType == needPulseType) idGotType = true

          result[nextPulseType]++
          Q.add(Pulse(dest, nextPulseType, module.id))
        }
      }

      curState = State(flipState.toMap(), conjState.toMap())
    }

    return result to curState
  }

  fun part1(input: List<String>): Long {
    val modules = input.toModules()
    var curState = modules.toInitState()

    val ans = LongArray(2)
    repeat(1000) {
      val (result, nextState) = modules.run(curState)
      curState = nextState
      ans[0] += result[0]
      ans[1] += result[1]
    }

    return ans[0] * ans[1]
  }

  fun part2(input: List<String>): Int {
    idGotType = false

    var cnt = 0
    val modules = input.toModules()
    var curState = modules.toInitState()

    while (true) {
      curState = modules.run(curState).second
      cnt ++

      if (idGotType) break
    }

    return cnt
  }


//  val testInput = readInput("Day20_test")
//  check(part1(testInput) == 32000000L)
//
//  val testInput1 = readInput("Day20_test1")
//  check(part1(testInput1) == 11687500L)
//
  val input = readInput("Day20")
//  part1(input).println()
  part2(input).println()
}
